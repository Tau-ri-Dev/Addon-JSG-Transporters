package dev.tauri.jsgtransporters.common.block.lantean_transporter;

import dev.tauri.jsgcore.common.block.JSGTabbedBlock;
import dev.tauri.jsgcore.common.block.util.IItemBlock;
import dev.tauri.jsgcore.common.item.JSGBlockItem;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.common.item.lantean_transporter.LanteanTransporterFrameItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
                defaultBlockState().setValue(BlockStateProperties.UP, false)
                        .setValue(BlockStateProperties.DOWN, false)
                        .setValue(BlockStateProperties.NORTH, false)
                        .setValue(BlockStateProperties.SOUTH, false)
                        .setValue(BlockStateProperties.EAST, false)
                        .setValue(BlockStateProperties.WEST, false)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    private boolean canConnectTo(BlockGetter level, BlockPos framePos, Direction direction) {
        BlockPos neighborPos = framePos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);
        VoxelShape faceShape = neighborState
                .getShape(level, neighborPos, CollisionContext.empty())
                .getFaceShape(direction.getOpposite());
        if (faceShape.isEmpty()) return false;

        final double faceInset = 0.001;

        for (int u = 0; u < 6; u++) {
            double uStart = (5.0 + u) / 16.0;
            double uEnd = uStart + 1.0 / 16.0;
            for (int v = 0; v < 6; v++) {
                boolean isBorder = u == 0 || u == 5 || v == 0 || v == 5;
                if (!isBorder) continue;

                double vStart = (5.0 + v) / 16.0;
                double vEnd = vStart + 1.0 / 16.0;

                AABB pixel = switch (direction) {
                    case UP -> new AABB(uStart, 1.0 - faceInset, vStart, uEnd, 1.0, vEnd);
                    case DOWN -> new AABB(uStart, 0.0, vStart, uEnd, faceInset, vEnd);
                    case NORTH -> new AABB(uStart, vStart, 0.0, uEnd, vEnd, faceInset);
                    case SOUTH -> new AABB(uStart, vStart, 1.0 - faceInset, uEnd, vEnd, 1.0);
                    case WEST -> new AABB(0.0, uStart, vStart, faceInset, uEnd, vEnd);
                    case EAST -> new AABB(1.0 - faceInset, uStart, vStart, 1.0, uEnd, vEnd);
                };
                if (!Shapes.joinIsNotEmpty(faceShape, Shapes.create(pixel), net.minecraft.world.phys.shapes.BooleanOp.AND)) return false;
            }
        }
        return true;
    }

    private BlockState getConnectedState(BlockState state, BlockGetter level, BlockPos pos) {
        return state
                .setValue(BlockStateProperties.UP, canConnectTo(level, pos, Direction.UP))
                .setValue(BlockStateProperties.DOWN, canConnectTo(level, pos, Direction.DOWN))
                .setValue(BlockStateProperties.NORTH, canConnectTo(level, pos, Direction.NORTH))
                .setValue(BlockStateProperties.SOUTH, canConnectTo(level, pos, Direction.SOUTH))
                .setValue(BlockStateProperties.EAST, canConnectTo(level, pos, Direction.EAST))
                .setValue(BlockStateProperties.WEST, canConnectTo(level, pos, Direction.WEST));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.getLevel().getFluidState(pos);
        BlockState state = defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
        return getConnectedState(state, context.getLevel(), pos);
    }

    @Override
    @Nonnull
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction,
                                  @Nonnull BlockState neighborState, @Nonnull LevelAccessor level,
                                  @Nonnull BlockPos currentPos, @Nonnull BlockPos neighborPos) {
        BlockState updated = getConnectedState(state, level, currentPos);
        if (updated.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return updated;
    }

    // @Override
    // @Nonnull
    // public FluidState getFluidState(@Nonnull BlockState state) {
    //     return state.getValue(BlockStateProperties.WATERLOGGED)
    //             ? Fluids.WATER.getSource(false)
    //             : super.getFluidState(state);
    // }

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
