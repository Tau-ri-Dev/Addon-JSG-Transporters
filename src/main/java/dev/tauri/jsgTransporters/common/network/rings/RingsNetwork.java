package dev.tauri.jsgTransporters.common.network.rings;

import dev.tauri.jsgTransporters.JSGTransporters;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class RingsNetwork extends SavedData{
  public static final RingsNetwork INSTANCE = new RingsNetwork();
  public static final String DATA_NAME = JSGTransporters.MOD_ID + "_transport_rings";
  private RingsNetwork() {
  }
  @Override
  public CompoundTag save(CompoundTag pCompoundTag) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }
  public void removeRings(BlockPos pos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeRings'");
  }

}
