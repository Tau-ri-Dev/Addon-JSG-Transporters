package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import dev.tauri.jsg.api.block.util.IItemBlock;
import dev.tauri.jsg.api.item.JSGBlockItem;
import dev.tauri.jsg.block.TickableBEBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LanteanTransporterAccessPanel extends TickableBEBlock implements IItemBlock, SimpleWaterloggedBlock {

    public LanteanTransporterAccessPanel(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity( BlockPos pPos, BlockState pState) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'newBlockEntity'");
    }

    @Override
    public JSGBlockItem getItemBlock() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getItemBlock'");
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> blockStateBuilder) {
    blockStateBuilder.add(BlockStateProperties.HORIZONTAL_FACING).add(BlockStateProperties.WATERLOGGED);
    super.createBlockStateDefinition(blockStateBuilder);
    }
}
