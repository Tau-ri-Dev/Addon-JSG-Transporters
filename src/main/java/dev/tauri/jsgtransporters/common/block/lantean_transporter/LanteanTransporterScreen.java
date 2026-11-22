package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.blockstate.JSGProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LanteanTransporterScreen extends JSGTabbedBlock implements SimpleWaterloggedBlock {
    public static final Properties SCREEN_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion();

    public LanteanTransporterScreen() {
        super(SCREEN_PROPS);
        this.registerDefaultState(
                defaultBlockState()
        );
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED).add(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        super.createBlockStateDefinition(pBuilder);
    }
    
}
