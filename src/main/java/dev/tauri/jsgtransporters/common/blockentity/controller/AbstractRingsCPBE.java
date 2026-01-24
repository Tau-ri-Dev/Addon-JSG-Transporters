package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsg.api.registry.ScheduledTaskType;
import dev.tauri.jsg.api.stargate.network.address.symbol.SymbolInterface;
import dev.tauri.jsg.api.state.State;
import dev.tauri.jsg.api.state.StateType;
import dev.tauri.jsg.api.util.ScheduledTask;
import dev.tauri.jsg.api.util.blockentity.IPreparable;
import dev.tauri.jsg.api.util.blockentity.ITickable;
import dev.tauri.jsg.api.util.blockentity.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.blockentity.util.ILinkable;
import dev.tauri.jsg.helpers.LinkingHelper;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.StateUpdatePacketToClient;
import dev.tauri.jsg.sound.JSGSoundHelper;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.registry.RingsScheduledTaskType;
import dev.tauri.jsgtransporters.common.registry.SoundRegistry;
import dev.tauri.jsgtransporters.common.state.renderer.RingsCPButtonPushedState;
import dev.tauri.jsgtransporters.common.state.renderer.RingsControlPanelRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractRingsCPBE extends BlockEntity implements ILinkable<RingsAbstractBE>, ITickable, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {
    public AbstractRingsCPBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean busy = false;

    @Override
    public boolean prepareBE() {
        busy = false;
        scheduledTasks.clear();
        if (isLinked()) {
            var linked = getLinkedDevice();
            if (linked != null)
                linked.setLinkedDevice(null);
            setLinkedDevice(null);
        }
        setChanged();
        return true;
    }

    public void onBroken() {
        if (isLinked()) {
            Objects.requireNonNull(getLinkedDevice()).setLinkedDevice(null);
        }
        setLinkedDevice(null);
    }


    // ------------------------------------------------------------------------
    // Scheduled tasks

    /**
     * List of scheduled tasks to be performed on {@link ITickable#tick(Level)()}.
     */
    protected List<ScheduledTask> scheduledTasks = new ArrayList<>();

    @Override
    public void addTask(ScheduledTask task) {
        if (level == null) return;
        task.setExecutor(this);
        task.setTaskCreated(level.getGameTime());
        scheduledTasks.add(task);
    }

    @Override
    public void executeTask(ScheduledTaskType enumScheduledTask, @Nullable CompoundTag compoundTag) {
        if (Objects.requireNonNull(enumScheduledTask) == RingsScheduledTaskType.RINGS_SYMBOL_DEACTIVATE) {
            busy = false;
            setChanged();
            sendState(StateType.RENDERER_UPDATE, new RingsCPButtonPushedState(true));
        }
    }

    @Override
    public State getState(StateType stateTypeEnum) {
        if (stateTypeEnum == StateType.RENDERER_UPDATE) {
            return new RingsCPButtonPushedState();
        }
        return null;
    }

    @Override
    public State createState(StateType stateTypeEnum) {
        if (stateTypeEnum == StateType.RENDERER_UPDATE) {
            return new RingsCPButtonPushedState();
        }
        return null;
    }

    @Override
    public void setState(StateType stateTypeEnum, State state) {
        if (level == null) return;
        if (stateTypeEnum == StateType.RENDERER_UPDATE) {
            var pushState = (RingsCPButtonPushedState) state;
            if (pushState.dim)
                getRendererStateClient().clearSymbols(level.getGameTime());
            else
                getRendererStateClient().activateSymbol(level.getGameTime(), pushState.getSymbol());
        }
    }

    @Override
    public void sendState(StateType type, State state) {
        if (Objects.requireNonNull(getLevel()).isClientSide) {
            return;
        }
        if (targetPoint != null) {
            JSGPacketHandler.sendToClient(new StateUpdatePacketToClient(getBlockPos(), type, state), targetPoint);
        } else {
            JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
        }
    }

    protected PacketDistributor.TargetPoint targetPoint;

    @Override
    public void onLoad() {
        if (!Objects.requireNonNull(getLevel()).isClientSide) {
            var pos = getBlockPos();
            this.targetPoint = new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());
        }
        super.onLoad();
    }

    @Override
    public void tick(@NotNull Level level) {
        // Scheduled tasks
        ScheduledTask.iterate(scheduledTasks, level.getGameTime());
    }

    // ------------------------------------------------------------------------
    // NBT

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        if (isLinked(true))
            compound.putLong("linkedPos", linkedPos.asLong());

        compound.put("scheduledTasks", ScheduledTask.serializeList(scheduledTasks));
        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        if (compound.contains("linkedPos"))
            linkedPos = BlockPos.of(compound.getLong("linkedPos"));
        ScheduledTask.deserializeList(compound.getCompound("scheduledTasks"), scheduledTasks, this);

        super.load(compound);
    }

    public abstract RingsControlPanelRendererState getRendererStateClient();

    public void pushSymbolButton(SymbolInterface symbol, @Nullable ServerPlayer player) {
        if (busy) return;
        if (!isLinked() || getLinkedDevice() == null || level == null || level.isClientSide()) return;
        busy = true;
        var result = getLinkedDevice().addSymbolToAddress(symbol);
        if (result != null && !result.ok() && player != null)
            player.displayClientMessage(result.component(), true);
        addTask(new ScheduledTask(RingsScheduledTaskType.RINGS_SYMBOL_DEACTIVATE, 10));
        if (symbol.origin())
            JSGSoundHelper.playSoundEvent(level, getBlockPos(), SoundRegistry.RINGS_GOAULD_BUTTON_DIAL);
        else
            JSGSoundHelper.playSoundEvent(level, getBlockPos(), SoundRegistry.RINGS_GOAULD_BUTTON);
        sendState(StateType.RENDERER_UPDATE, new RingsCPButtonPushedState(symbol));
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
    public @Nullable RingsAbstractBE getLinkedDevice() {
        if (level == null) return null;
        if (linkedPos == null) return null;
        if (level.getBlockEntity(linkedPos) instanceof RingsAbstractBE rings) return rings;
        return null;
    }

    @Override
    public @Nullable BlockPos getLinkedPos() {
        return linkedPos;
    }

    public abstract TagKey<Block> getRingsBlocks();

    public void updateLinkStatus() {
        if (level == null) return;
        var pos = getBlockPos();
        var block = getRingsBlocks();
        if (block == null) return;
        BlockPos closesRings = LinkingHelper.findClosestUnlinked(level, pos, LinkingHelper.getDhdRange(), block);

        if (closesRings != null && level.getBlockEntity(closesRings) instanceof RingsAbstractBE be) {
            be.setLinkedDevice(pos);
            setLinkedDevice(closesRings);
            setChanged();
        }
    }
}
