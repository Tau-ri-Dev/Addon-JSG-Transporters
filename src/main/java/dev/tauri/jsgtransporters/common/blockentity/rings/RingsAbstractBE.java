package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.sound.JSGSoundHelper;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.ITickable;
import dev.tauri.jsg.util.JSGAxisAlignedBB;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.SoundRegistry;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddress;
import dev.tauri.jsgtransporters.common.rings.network.RingsNetwork;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

// Contemplating making rings a multiblock
public abstract class RingsAbstractBE extends BlockEntity implements ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {

    public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected Map<SymbolTypeEnum<?>, RingsAddress> addressMap = new HashMap<>();
    protected RingsPos ringsPos;

    @Override
    public boolean prepareBE() {
        this.needRegenerate = true;
        setChanged();
        return true;
    }

    @Override
    public BlockPos m_58899_() {
        return worldPosition;
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
    }

    @Override
    public void executeTask(EnumScheduledTask task, CompoundTag context) {
        switch (task) {
            case RINGS_START_ANIMATION:
                if (level == null) break;
                rendererState.startAnimation(level.getGameTime());
                setChanged();
                getAndSendState(StateTypeEnum.RENDERER_STATE);
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

    public void test() {
        addTask(new ScheduledTask(EnumScheduledTask.RINGS_START_ANIMATION, (int) (1.67f * 20)));
        JSGSoundHelper.playPositionedSound(level, getBlockPos(), SoundRegistry.RINGS_TRANSPORT, true);
    }

    // ------------------------------------------------------------------------
    // NBT
    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        for (var address : addressMap.values()) {
            compound.put("address_" + address.getSymbolType(), address.serializeNBT());
        }
        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        for (SymbolTypeEnum<?> symbolType : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            if (compound.contains("address_" + symbolType))
                addressMap.put(symbolType, new RingsAddress(compound.getCompound("address_" + symbolType)));
        }
        super.load(compound);
    }
}
