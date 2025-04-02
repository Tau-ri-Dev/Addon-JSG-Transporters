package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.chunkloader.ChunkManager;
import dev.tauri.jsg.helpers.LinkingHelper;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
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
import dev.tauri.jsgtransporters.common.registry.SoundRegistry;
import dev.tauri.jsgtransporters.common.rings.network.*;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
                address = new RingsAddress(symbolType);
                address.generate(random);
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
                if (context == null) {
                    rendererState.startAnimation(level.getGameTime());
                    setChanged();
                    getAndSendState(StateTypeEnum.RENDERER_STATE);
                    context = new CompoundTag();
                    context.putBoolean("end", true);
                    addTask(new ScheduledTask(task, RING_ANIMATION_LENGTH, context));
                } else {
                    if (context.getBoolean("end")) {
                        ChunkManager.unforceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
                        busy = false;
                        targetRings = null;
                        setChanged();
                    }
                }
                break;
            default:
                break;
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
        dialedAddress.addSymbol(symbol);
        if (dialedAddress.getSize() > 4) {
            tryConnect();
            dialedAddress.clear();
        }
    }

    public boolean busy = false;
    public RingsPos targetRings;

    public void tryConnect() {
        if (level == null || level.isClientSide()) return;
        if (dialedAddress.size() < 5) {
            JSGTransporters.logger.info("Address < 5");
            return;
        }
        if (!dialedAddress.getLast().origin()) {
            JSGTransporters.logger.info("Address no origin");
            return;
        }

        var rings = RingsNetwork.INSTANCE.getRings(dialedAddress.toImmutable());
        if (rings == null) {
            JSGTransporters.logger.info("Target rings null");
            return;
        }
        var ringsBe = rings.getBlockEntity();
        if (ringsBe.busy) {
            JSGTransporters.logger.info("Target rings busy");
            return;
        }

        targetRings = rings;
        setChanged();
        teleport();

        ringsBe.targetRings = ringsPos;
        ringsBe.setChanged();
        ringsBe.teleport();
        JSGTransporters.logger.info("Target rings OK");
    }

    protected void teleport() {
        if (level == null || level.isClientSide()) return;
        ChunkManager.forceChunk((ServerLevel) level, new ChunkPos(getBlockPos()));
        addTask(new ScheduledTask(EnumScheduledTask.RINGS_START_ANIMATION, (int) (1.67f * 20)));
        JSGSoundHelper.playPositionedSound(level, getBlockPos(), SoundRegistry.RINGS_TRANSPORT, true);
    }
}
