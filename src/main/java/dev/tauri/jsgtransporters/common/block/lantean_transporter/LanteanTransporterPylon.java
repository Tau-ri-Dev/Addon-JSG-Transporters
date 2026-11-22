package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import javax.annotation.Nonnull;

import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.block.util.IItemBlock;
import dev.tauri.jsgcore.common.item.JSGBlockItem;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.common.item.lantean_transporter.LanteanTransporterPylonItem;
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

public class LanteanTransporterPylon extends JSGTabbedBlock implements IItemBlock, SimpleWaterloggedBlock {
    public static final Properties PYLON_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL);

    public LanteanTransporterPylon() {
        super(PYLON_PROPS);
        registerDefaultState(
            defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new LanteanTransporterPylonItem(this, new Item.Properties(), getTabs());
    }

    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        return List.of(CoreTabs.TAB_TRANSPORTATION);
    }
}
