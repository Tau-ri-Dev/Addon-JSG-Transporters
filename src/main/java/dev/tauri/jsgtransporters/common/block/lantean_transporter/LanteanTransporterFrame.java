package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.block.util.IItemBlock;
import dev.tauri.jsgcore.common.item.JSGBlockItem;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.common.item.lantean_transporter.LanteanTransporterFrameItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.util.function.Supplier;

public class LanteanTransporterFrame extends JSGTabbedBlock implements IItemBlock, SimpleWaterloggedBlock {
    public static final Properties FRAME_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL);
    public LanteanTransporterFrame() {
        super(FRAME_PROPS);
        this.registerDefaultState(
                defaultBlockState().setValue(BlockStateProperties.UP, true)
                        .setValue(BlockStateProperties.DOWN, true)
                        .setValue(BlockStateProperties.NORTH, true)
                        .setValue(BlockStateProperties.SOUTH, true)
                        .setValue(BlockStateProperties.EAST, true)
                        .setValue(BlockStateProperties.WEST, true)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.UP).add(BlockStateProperties.DOWN).add(BlockStateProperties.NORTH)
                .add(BlockStateProperties.SOUTH).add(BlockStateProperties.EAST).add(BlockStateProperties.WEST)
                .add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new LanteanTransporterFrameItem(this, new net.minecraft.world.item.Item.Properties(), getTabs());
    }

    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        return List.of(CoreTabs.TAB_TRANSPORTATION);
    }
}
