package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.stargate.StargateAbstractBaseBE;
import dev.tauri.jsg.blockentity.stargate.StargateAbstractMemberBE;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.chunkloader.ChunkManager;
import dev.tauri.jsg.helpers.LinkingHelper;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.registry.BlockRegistry;
import dev.tauri.jsg.sound.JSGSoundHelper;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.ILinkable;
import dev.tauri.jsg.util.ITickable;
import dev.tauri.jsg.util.JSGAxisAlignedBB;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper;
import dev.tauri.jsgtransporters.common.registry.SoundRegistry;
import dev.tauri.jsgtransporters.common.rings.RingsConnectResult;
import dev.tauri.jsgtransporters.common.rings.network.*;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class RingsAbstractBE extends BlockEntity implements ILinkable<AbstractRingsCPBE>, ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {

    public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected Map<SymbolTypeEnum<?>, RingsAddress> addressMap = new HashMap<>();
    protected RingsPos ringsPos;
    protected RingsAddressDynamic dialedAddress = new RingsAddressDynamic(getSymbolType());

    @Override
    public boolean prepareBE() {
        this.needRegenerate = true;
        setChanged();
        return true;
    }

    @Nonnull
    public Level getLevelNotNull() {
        return Objects.requireNonNull(getLevel());
    }

    public long getTime() {
        return getLevelNotNull().getGameTime();
    }

    protected TargetPoint targetPoint;
    protected BlockPos pos;

    @Override
    public void onLoad() {
        if (!Objects.requireNonNull(getLevel()).isClientSide) {
            this.pos = getBlockPos();
            this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());

            generateAddresses(false);
        }
        super.onLoad();
    }

    private boolean needRegenerate = false;

    private boolean addedToNetwork;

    @Override
    public void tick() {
        // Scheduled tasks
        ScheduledTask.iterate(scheduledTasks, getTime());
        if (!getLevelNotNull().isClientSide) {
            if (!addedToNetwork) {
                addedToNetwork = true;
                //getDeviceHolder().connectToWirelessNetwork();
            }
        }
    }

    public void onBroken() {
        initRingsPos();
        RingsNetwork.INSTANCE.removeRings(ringsPos);
        if (isLinked()) {
            Objects.requireNonNull(getLinkedDevice()).setLinkedDevice(null);
        }
        setLinkedDevice(null);
    }

    @Nullable
    public RingsAddress getRingsAddress(SymbolTypeEnum<?> symbolType) {
        if (addressMap == null) return null;

        return addressMap.get(symbolType);
    }

    public void generateAddresses(boolean reset) {
        if (reset && ringsPos != null)
            RingsNetwork.INSTANCE.removeRings(ringsPos);
        Random random = new Random(pos.hashCode() * 31L + getLevelNotNull().dimension().location().hashCode());

        for (SymbolTypeEnum<?> symbolType : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            var address = getRingsAddress(symbolType);

            if (address == null || reset) {
                do {
                    address = new RingsAddress(symbolType);
                    address.generate(random);
                } while (RingsNetwork.INSTANCE.getAllAddresses().contains(address));
            }

            this.setRingsAddress(symbolType, address);
        }
    }

    public abstract SymbolTypeEnum<?> getSymbolType();

    protected void initRingsPos() {
        ringsPos = new RingsPos(getLevelNotNull().dimension(), getBlockPos(), getSymbolType());
    }

    public void setRingsAddress(SymbolTypeEnum<?> symbolType, RingsAddress address) {
        initRingsPos();
        addressMap.put(symbolType, address);
        RingsNetwork.INSTANCE.putRings(address, ringsPos);
        setChanged();
    }


    // ------------------------------------------------------------------------
    // Scheduled tasks

    /**
     * List of scheduled tasks to be performed on {@link ITickable#tick()()}.
     */
    protected List<ScheduledTask> scheduledTasks = new ArrayList<>();

    @Override
    public void addTask(ScheduledTask task) {
        task.setExecutor(this);
        task.setTaskCreated(getTime());
        scheduledTasks.add(task);
        setChanged();
    }

    @Override
    public void executeTask(EnumScheduledTask task, CompoundTag context) {
        switch (task) {
            case RINGS_START_ANIMATION:
                if (level == null) break;
                if (level.isClientSide()) break;
                if (context == null) {
                    rendererState.startAnimation(level.getGameTime());
                    setChanged();
                    getAndSendState(StateTypeEnum.RENDERER_STATE);
                    var c = new CompoundTag();
                    c.putBoolean("end", true);
                    addTask(new ScheduledTask(task, RING_ANIMATION_LENGTH, c));
                    addTask(new ScheduledTask(EnumScheduledTask.RINGS_SOLID_BLOCKS, 10));
                    addTask(new ScheduledTask(EnumScheduledTask.RINGS_SOLID_BLOCKS, RING_ANIMATION_LENGTH, new CompoundTag()));

                    var offset = getVerticalOffset();
                    for (int i = 0; i < 3; i++) {
                        var c2 = new CompoundTag();
                        c2.putBoolean("tp", true);
                        c2.putInt("index", i);
                        addTask(new ScheduledTask(task, 40 + ((offset > 0 ? (2 - i) : i) * 10), c2));
                    }
                    break;
                } else {
                    if (context.getBoolean("end")) {
                        ChunkManager.unforceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
                        busy = false;
                        targetRings = null;
                        break;
                    } else if (context.getBoolean("tp") && context.contains("index")) {
                        if (targetRings == null) break;
                        teleportVolumes(context.getInt("index"));
                        break;
                    }
                }
                break;
            case RINGS_SOLID_BLOCKS:
                setBorderBlocks(context != null);
                break;
            default:
                break;
        }
        setChanged();
    }

    protected void setBorderBlocks(boolean clear) {
        if (level == null || level.isClientSide()) return;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x != -2 && x != 2 && z != 2 && z != -2) continue;
                for (int y = 0; y < 3; y++) {
                    var pos = getBlockPos().offset(x, getVerticalOffset() + y, z);
                    var state = level.getBlockState(pos);
                    if (clear) {
                        if (state.getBlock() != BlockRegistry.INVISIBLE_BLOCK.get()) continue;
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        continue;
                    }
                    level.setBlock(pos, BlockRegistry.INVISIBLE_BLOCK.get().defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void sendState(StateTypeEnum type, State state) {
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            return;
        }
        if (targetPoint != null) {
            JSGPacketHandler.sendToClient(new StateUpdatePacketToClient(getBlockPos(), type, state), targetPoint);
        } else {
            JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
        }
    }

    protected final ComputerDeviceHolder deviceHolder = new ComputerDeviceHolder(this);

    @Override
    public ComputerDeviceHolder getDeviceHolder() {
        return deviceHolder;
    }

    public RingsRendererState rendererState = new RingsRendererState();

    public RingsRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new JSGAxisAlignedBB(
                getBlockPos().offset(-3, -5, -3),
                getBlockPos().offset(3, 5, 3)
        );
    }

    @Override
    public State getState(@NotNull StateTypeEnum stateType) {
        return switch (stateType) {
            case RENDERER_STATE -> getRendererStateClient();
            default -> null;
        };
    }

    @Override
    public State createState(@NotNull StateTypeEnum stateType) {
        return switch (stateType) {
            case RENDERER_STATE -> new RingsRendererState();
            default -> null;
        };
    }

    @Override
    public void setState(@NotNull StateTypeEnum stateType, @NotNull State state) {
        switch (stateType) {
            case RENDERER_STATE:
                rendererState = (RingsRendererState) state;
                setChanged();
                break;
            default:
                break;
        }
    }


    public static final int RING_ANIMATION_LENGTH = (int) (20 * 4.91f);

    // ------------------------------------------------------------------------
    // NBT
    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        for (var address : addressMap.values()) {
            compound.put("address_" + address.getSymbolType(), address.serializeNBT());
        }
        if (isLinked(true))
            compound.putLong("linkedPos", linkedPos.asLong());
        compound.putBoolean("busy", busy);
        if (targetRings != null)
            compound.put("targetRings", targetRings.serializeNBT());
        compound.put("scheduledTasks", ScheduledTask.serializeList(scheduledTasks));

        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        for (SymbolTypeEnum<?> symbolType : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            if (compound.contains("address_" + symbolType))
                addressMap.put(symbolType, new RingsAddress(compound.getCompound("address_" + symbolType)));
        }
        if (compound.contains("linkedPos"))
            linkedPos = BlockPos.of(compound.getLong("linkedPos"));
        busy = compound.getBoolean("busy");
        if (compound.contains("targetRings"))
            targetRings = new RingsPos(compound.getCompound("targetRings"));
        ScheduledTask.deserializeList(compound.getCompound("scheduledTasks"), scheduledTasks, this);

        super.load(compound);
    }

    private BlockPos linkedPos;

    @Override
    public boolean canLinkTo() {
        return (linkedPos == null);
    }

    @Override
    public void setLinkedDevice(BlockPos blockPos) {
        linkedPos = blockPos;
        setChanged();
    }

    @Override
    public @Nullable AbstractRingsCPBE getLinkedDevice() {
        if (level == null) return null;
        if (linkedPos == null) return null;
        if (level.getBlockEntity(linkedPos) instanceof AbstractRingsCPBE cp) return cp;
        return null;
    }

    @Override
    public @Nullable BlockPos getLinkedPos() {
        return linkedPos;
    }

    public abstract Block getControlPanelBlock();

    public void updateLinkStatus() {
        pos = getBlockPos();
        var block = getControlPanelBlock();
        if (block == null) return;
        BlockPos closestCP = LinkingHelper.findClosestUnlinked(getLevelNotNull(), pos, LinkingHelper.getDhdRange(), block);

        if (closestCP != null && getLevelNotNull().getBlockEntity(closestCP) instanceof AbstractRingsCPBE be) {
            be.setLinkedDevice(pos);
            setLinkedDevice(closestCP);
            setChanged();
        }
    }

    public void addSymbolToAddress(SymbolInterface symbol) {
        if (dialedAddress.size() > 4) dialedAddress.clear();
        dialedAddress.addSymbol(symbol);
        if (dialedAddress.getSize() > 4 || symbol.origin()) {
            tryConnect();
            dialedAddress.clear();
        }
    }

    public boolean busy = false;
    public RingsPos targetRings;
    public boolean outbound = false;

    @NotNull
    public RingsConnectResult tryConnect() {
        if (level == null || level.isClientSide()) return RingsConnectResult.CLIENT;
        if (dialedAddress.size() < 5) {
            return RingsConnectResult.ADDRESS_MALFORMED;
        }
        if (!dialedAddress.getLast().origin()) {
            return RingsConnectResult.NO_ORIGIN;
        }

        var rings = RingsNetwork.INSTANCE.getRings(dialedAddress.toImmutable());
        if (rings == null) {
            return RingsConnectResult.ADDRESS_MALFORMED;
        }
        var ringsBe = rings.getBlockEntity();
        if (ringsBe.busy) {
            return RingsConnectResult.BUSY;
        }

        outbound = true;
        targetRings = rings;
        setChanged();
        startTeleportAnimation();

        ringsBe.outbound = false;
        ringsBe.targetRings = ringsPos;
        ringsBe.setChanged();
        ringsBe.startTeleportAnimation();
        return RingsConnectResult.OK;
    }

    public int getVerticalOffset() {
        return 2;
    }

    protected void startTeleportAnimation() {
        if (level == null || level.isClientSide()) return;
        ignoredEntities.clear();
        ChunkManager.forceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
        addTask(new ScheduledTask(EnumScheduledTask.RINGS_START_ANIMATION, (int) (1.67f * 20)));
        JSGSoundHelper.playPositionedSound(level, getBlockPos(), SoundRegistry.RINGS_TRANSPORT, true);
    }

    public final List<Entity> ignoredEntities = new ArrayList<>();

    protected void teleportVolumes(int index) {
        if (targetRings == null) return;
        if (level == null) return;
        if (index < 0) return;
        var targetRings = this.targetRings.getBlockEntity();

        var minPos = new BlockPos(-1, getVerticalOffset() + index, -1).offset(getBlockPos());
        var maxPos = new BlockPos(1, getVerticalOffset() + index, 1).offset(getBlockPos());
        var poses = BlockPos.betweenClosed(minPos, maxPos);
        var entities = level.getEntities(null, new JSGAxisAlignedBB(minPos.getCenter(), maxPos.getCenter()).grow(0.5, 0.5, 0.5));
        for (var e : entities) {
            if (ignoredEntities.contains(e)) continue;
            targetRings.ignoredEntities.add(e);
            TeleportHelper.teleportEntity(e, ringsPos, this.targetRings);
        }

        if (!outbound || targetRings.level == null) return;
        poses.forEach(pos -> {
            var imPos = pos.immutable();
            if (imPos == this.getBlockPos()) return;
            if (imPos == this.getLinkedPos()) return;
            var relativePos = imPos.subtract(getBlockPos());
            var targetPos = targetRings.getBlockPos().offset(relativePos);

            var stateTarget = targetRings.level.getBlockState(targetPos);
            CompoundTag targetTag = null;
            var entity = targetRings.level.getBlockEntity(targetPos);
            if (entity instanceof RingsAbstractBE) return;
            if (entity instanceof StargateAbstractBaseBE) return;
            if (entity instanceof StargateAbstractMemberBE) return;
            if (entity != null) {
                targetTag = entity.serializeNBT();
                if (entity instanceof Container chest) {
                    chest.clearContent();
                    chest.setChanged();
                }
            }
            var thisState = level.getBlockState(imPos);
            CompoundTag thisTag = null;
            entity = level.getBlockEntity(imPos);
            if (entity instanceof RingsAbstractBE) return;
            if (entity instanceof StargateAbstractBaseBE) return;
            if (entity instanceof StargateAbstractMemberBE) return;
            if (entity != null) {
                thisTag = entity.serializeNBT();
                if (entity instanceof Container chest) {
                    chest.clearContent();
                    chest.setChanged();
                }
            }

            var flag = 2 | 32 | 16;

            level.setBlock(imPos, Blocks.AIR.defaultBlockState(), flag);
            targetRings.level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), flag);

            targetRings.level.setBlock(targetPos, thisState, flag);
            entity = targetRings.level.getBlockEntity(targetPos);
            if (thisTag != null && entity != null) {
                entity.deserializeNBT(thisTag);
                entity.setChanged();
            }

            level.setBlock(imPos, stateTarget, flag);
            entity = level.getBlockEntity(imPos);
            if (targetTag != null && entity != null) {
                entity.deserializeNBT(targetTag);
                entity.setChanged();
            }
        });
    }
}
