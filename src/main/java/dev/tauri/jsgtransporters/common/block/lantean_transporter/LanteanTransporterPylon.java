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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;

import java.util.List;
import java.util.function.Supplier;

public class LanteanTransporterPylon extends JSGTabbedBlock implements IItemBlock, SimpleWaterloggedBlock {
    public static final IntegerProperty PYLON_PART = IntegerProperty.create("part", 0, 2);

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
                .setValue(PYLON_PART, 0)
                .setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PYLON_PART).add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (pos.getY() > level.getMaxBuildHeight() - 3) return null;
        if (!level.getBlockState(pos.above()).canBeReplaced(context)
                || !level.getBlockState(pos.above(2)).canBeReplaced(context)) return null;

        FluidState fluid = level.getFluidState(pos);
        return defaultBlockState()
                .setValue(PYLON_PART, 0)
                .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state,
                            @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide) return;

        level.setBlock(pos.above(), state
                .setValue(PYLON_PART, 1)
                .setValue(BlockStateProperties.WATERLOGGED,
                        level.getFluidState(pos.above()).getType() == Fluids.WATER), 3);
        level.setBlock(pos.above(2), state
                .setValue(PYLON_PART, 2)
                .setValue(BlockStateProperties.WATERLOGGED,
                        level.getFluidState(pos.above(2)).getType() == Fluids.WATER), 3);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction,
                                  @Nonnull BlockState neighborState, @Nonnull LevelAccessor level,
                                  @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        int part = state.getValue(PYLON_PART);
        if (direction == Direction.DOWN && part == 0) {
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }
        if (!level.isClientSide()) {
            BlockPos basePos = switch (part) {
                case 1 -> pos.below();
                case 2 -> pos.below(2);
                default -> pos;
            };
            if (!isPylonColumn(level, basePos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private boolean isPylonColumn(LevelAccessor level, BlockPos basePos) {
        BlockState lower = level.getBlockState(basePos);
        BlockState middle = level.getBlockState(basePos.above());
        BlockState upper = level.getBlockState(basePos.above(2));
        return lower.is(this)
                && lower.getValue(PYLON_PART) == 0
                && middle.is(this)
                && middle.getValue(PYLON_PART) == 1
                && upper.is(this)
                && upper.getValue(PYLON_PART) == 2;
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state,
                                  @Nonnull Player player) {
        if (!level.isClientSide && !player.isCreative()) {
            removeOtherPylonParts(level, pos, state);
        }
        super.playerWillDestroy(level, pos, state, player);
    }


    private void removeOtherPylonParts(Level level, BlockPos pos, BlockState state) {
        int part = state.getValue(PYLON_PART);
        BlockPos basePos = switch (part) {
            case 1 -> pos.below();
            case 2 -> pos.below(2);
            default -> pos;
        };

        for (int i = 0; i < 3; i++) {
            BlockPos pylonPos = basePos.above(i);
            if (pylonPos.equals(pos)) continue;
            level.setBlock(pylonPos, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(@Nonnull BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new LanteanTransporterPylonItem(this, new Item.Properties(), getTabs());
    }

    public List<Supplier<RegistryObjectWrapper<CreativeModeTab>>> getTabs() {
        return List.of(CoreTabs.TAB_TRANSPORTATION);
    }
}
