package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.ILinkable;
import dev.tauri.jsg.util.ITickable;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsControlPanelRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRingsCPBE extends BlockEntity implements ILinkable<RingsAbstractBE>, ITickable, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {
    public AbstractRingsCPBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public boolean prepareBE() {
        return true;
    }

    @Override
    public void addTask(ScheduledTask scheduledTask) {

    }

    @Override
    public void executeTask(EnumScheduledTask enumScheduledTask, @Nullable CompoundTag compoundTag) {

    }

    @Override
    public State getState(StateTypeEnum stateTypeEnum) {
        return null;
    }

    @Override
    public State createState(StateTypeEnum stateTypeEnum) {
        return null;
    }

    @Override
    public void setState(StateTypeEnum stateTypeEnum, State state) {

    }

    @Override
    public void sendState(StateTypeEnum stateTypeEnum, State state) {

    }

    @Override
    public void tick() {

    }

    // ------------------------------------------------------------------------
    // NBT

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        if (isLinked(true))
            compound.putLong("linkedPos", linkedPos.asLong());

        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        if (compound.contains("linkedPos"))
            linkedPos = BlockPos.of(compound.getLong("linkedPos"));

        super.load(compound);
    }

    public abstract RingsControlPanelRendererState getRendererStateClient();

    public void pushSymbolButton(SymbolInterface symbol, @Nullable ServerPlayer player, boolean force) {
        JSGTransporters.logger.info("Pushed button on the server! {}", symbol.getEnglishName());
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
}
