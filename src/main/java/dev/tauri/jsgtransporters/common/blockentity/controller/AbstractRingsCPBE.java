package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateProviderInterface;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsg.util.ITickable;
import dev.tauri.jsgtransporters.common.state.renderer.RingsControlPanelRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AbstractRingsCPBE extends BlockEntity implements ITickable, ScheduledTaskExecutorInterface, StateProviderInterface, IPreparable {
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

    protected RingsControlPanelRendererState rendererState = new RingsControlPanelRendererState();

    public RingsControlPanelRendererState getRendererStateClient() {
        return rendererState;
    }
}
