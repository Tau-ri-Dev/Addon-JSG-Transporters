package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import dev.tauri.jsgcore.common.block.util.IItemBlock;
import dev.tauri.jsgcore.common.item.JSGBlockItem;
import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.common.item.lantean_transporter.LanteanTransporterPanelingItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.util.function.Supplier;

public class LanteanTransporterPaneling extends JSGTabbedBlock implements IItemBlock, SimpleWaterloggedBlock {

    public static final Properties PANEL_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL);

    public LanteanTransporterPaneling() {
        super(PANEL_PROPS);
        this.registerDefaultState(
                defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new LanteanTransporterPanelingItem(this, new Item.Properties(), getTabs());
    }

    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        return List.of(CoreTabs.TAB_TRANSPORTATION);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(blockStateBuilder);
    }
}
