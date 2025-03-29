package dev.tauri.jsgTransporters.common.blockentity.rings;

import java.util.Objects;

import dev.tauri.jsg.blockentity.util.ScheduledTaskExecutorInterface;
import dev.tauri.jsg.integration.ComputerDeviceProvider;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.state.State;
import dev.tauri.jsgTransporters.JSGTransporters;
import dev.tauri.jsgTransporters.common.state.StateProvider;
import dev.tauri.jsgTransporters.common.state.StateType;
import dev.tauri.jsg.util.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor.TargetPoint;

// Contemplating making rings a multiblock
public abstract class RingsAbstractBE extends BlockEntity
    implements ITickable, ComputerDeviceProvider, ScheduledTaskExecutorInterface, StateProvider {

  public RingsAbstractBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void sendState(StateType type, State state) {
    if (Objects.requireNonNull(getLevel()).isClientSide) {
      return;
    }
    if (targetPoint != null) {
      JSGPacketHandler.sendToClient(state, targetPoint);
    }else {
      JSGTransporters.logger.debug("targetPoint as null trying to send {} from {}", this, this.getClass().getCanonicalName());
    }
  }

  protected TargetPoint targetPoint;
  protected BlockPos pos;

  @Override
    public void onLoad() {
      if (!Objects.requireNonNull(getLevel()).isClientSide){
        this.pos = getBlockPos();
        this.targetPoint = new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 512, Objects.requireNonNull(getLevel()).dimension());

        generateAddress(false);
      }
    super.onLoad();
    }

  public void generateAddress(boolean reset){
    if (reset){
      getNetwork().removeRings(pos);
    }
  }

  private static RingsNetwork getNetwork() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getNetwork'");
  }
}
