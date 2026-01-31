package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.api.block.JSGTabbedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LanteanTransporterFrame extends JSGTabbedBlock implements SimpleWaterloggedBlock {
  public LanteanTransporterFrame(Properties blockProperties) {
    super(blockProperties);
    //TODO Auto-generated constructor stub
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
    pBuilder.add(BlockStateProperties.UP).add(BlockStateProperties.DOWN).add(BlockStateProperties.NORTH).add(BlockStateProperties.SOUTH).add(BlockStateProperties.EAST).add(BlockStateProperties.WEST).add(BlockStateProperties.WATERLOGGED);
    super.createBlockStateDefinition(pBuilder);
  }
}
