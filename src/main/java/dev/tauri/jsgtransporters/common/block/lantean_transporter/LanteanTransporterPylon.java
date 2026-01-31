package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import javax.annotation.Nonnull;

import dev.tauri.jsg.api.block.JSGTabbedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LanteanTransporterPylon extends JSGTabbedBlock implements SimpleWaterloggedBlock {

  public LanteanTransporterPylon(Properties pProperties) {
    super(pProperties);
    //TODO Auto-generated constructor stub
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> pBuilder) {
    pBuilder.add(BlockStateProperties.HORIZONTAL_FACING).add(BlockStateProperties.WATERLOGGED);
    super.createBlockStateDefinition(pBuilder);
  }
}
