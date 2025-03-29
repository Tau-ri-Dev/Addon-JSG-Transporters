package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.ITickable;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.rings.network.RingsNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor.TargetPoint;

import java.util.Objects;

// Contemplating making rings a multiblock
public abstract class RingsAbstractBE extends BlockEntity implements ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {

    public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

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

    private boolean needRegenerate = false;

    @Override
    public void tick() {
    }

    @Override
    public void addTask(ScheduledTask arg0) {
    }

    @Override
    public void executeTask(EnumScheduledTask arg0, CompoundTag arg1) {
    }

    @Override
    public void sendState(StateTypeEnum type, State state) {
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            return;
        }
        if (targetPoint != null) {
            JSGPacketHandler.sendToClient(state, targetPoint);
        } else {
            JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
        }
    }

    protected TargetPoint targetPoint;
    protected BlockPos pos;

    @Override
    public void onLoad() {
        if (!Objects.requireNonNull(getLevel()).isClientSide) {
            this.pos = getBlockPos();
            this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());

            generateAddress(false);
        }
        super.onLoad();
    }

    public void generateAddress(boolean reset) {
        if (reset) {
            RingsNetwork.INSTANCE.removeRings(pos);
        }
    }

    protected final ComputerDeviceHolder deviceHolder = new ComputerDeviceHolder(this);

    @Override
    public ComputerDeviceHolder getDeviceHolder() {
        return deviceHolder;
    }
}
