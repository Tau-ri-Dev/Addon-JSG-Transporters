package dev.tauri.jsgTransporters.common.blockentity.rings;

import dev.tauri.jsg.blockentity.IPreparable;
import dev.tauri.jsg.blockentity.util.IUpgradable;
import dev.tauri.jsg.blockentity.util.ScheduledTask;
import dev.tauri.jsg.config.JSGConfig;
import dev.tauri.jsg.config.ingame.ITileConfig;
import dev.tauri.jsg.config.ingame.JSGConfigOption;
import dev.tauri.jsg.config.ingame.JSGTileEntityConfig;
import dev.tauri.jsg.integration.ComputerDeviceHolder;
import dev.tauri.jsg.stargate.EnumScheduledTask;
import dev.tauri.jsg.state.State;
import dev.tauri.jsg.state.StateTypeEnum;
import dev.tauri.jsgTransporters.common.registry.BlockConfigOptionRegistry;
import dev.tauri.jsgTransporters.common.state.StateType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RingsClassicBE extends RingsAbstractBE implements IUpgradable, ITileConfig, IPreparable {

  public RingsClassicBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
  }

  private ResourceLocation getConfigType() {
    return BlockConfigOptionRegistry.RINGS_COMMON;
  }
  JSGTileEntityConfig config;
  @Override
  public State getState(StateTypeEnum type) {
    return getState(StateType.convert(type));
  }
  
  @Override
  public JSGTileEntityConfig getConfig() {
    return config;
  }

  @Override
  public void initConfig() {
    this.config = new JSGTileEntityConfig(getConfigType());
  }


  @Override
  public void setConfig(JSGTileEntityConfig newConfig) {
    boolean changed = false;
    for (JSGConfigOption<?> opt : newConfig.getOptions()){
      changed = changed || this.config.getOption(opt.getLabel()).setValue(opt.getValue().toString());
    }
    if (changed){
      setChanged();
    }
  }

  @Override
  public void setConfigAndUpdate(JSGTileEntityConfig newConfig) {
    setConfig(newConfig);
    sendState(StateType.GUI_STATE, getState(StateType.GUI_STATE));
  }

  @Override
  public boolean prepareBE() {
    this.needRegenerate = true;
    setChanged();
    return true;
  }
  private boolean needRegenerate = false;

  @Override
  public void tick() {
  }

  @Override
  public ComputerDeviceHolder getDeviceHolder() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDeviceHolder'");
  }

  @Override
  public String getDeviceType() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDeviceType'");
  }

  @Override
  public void addTask(ScheduledTask arg0) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addTask'");
  }

  @Override
  public void executeTask(EnumScheduledTask arg0, CompoundTag arg1) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'executeTask'");
  }

  @Override
  public State getState(StateType stateType) {
    return switch(stateType){
      case GUI_STATE -> null; // TODO rings container gui state
      case GUI_UPDATE -> null; // TODO rings container gui update
      default -> null;
    };
  }

  @Override
  public State createState(StateType stateType) {
    return switch(stateType){
      case GUI_STATE -> null; // TODO rings container gui state
      case GUI_UPDATE -> null; // TODO rings container gui update
      default -> null;
    };
  }

  @Override
  public void setState(StateType stateType, State state) {
    switch(stateType){
      default -> {}
    }
  }
}
