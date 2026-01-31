package dev.tauri.jsgtransporters.common.blockentity.lantean_transporter;

import org.jetbrains.annotations.NotNull;

import dev.tauri.jsg.api.multistructure.IMultiStructureBE;
import dev.tauri.jsg.api.util.blockentity.IPreparable;
import dev.tauri.jsg.api.util.blockentity.ITickable;
import dev.tauri.jsgtransporters.common.multistructure.mergehelper.LanteanTransporterMergeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LanteanTransporterBE extends BlockEntity implements ITickable, IPreparable/*, IMultiStructureBE<LanteanTransporterMergeHelper>*/ {

  public LanteanTransporterBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    //TODO Auto-generated constructor stub
  }

  @Override
  public void tick(@NotNull Level arg0) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'tick'");
  }
/* 
  @Override
  public LanteanTransporterMergeHelper getMergeHelper() {
    return new LanteanTransporterMergeHelper(this);
  }
  //*/

  @Override
  public boolean prepareBE() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'prepareBE'");
  }
}
