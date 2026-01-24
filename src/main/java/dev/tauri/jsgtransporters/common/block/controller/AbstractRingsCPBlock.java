package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.api.block.util.IHighlightBlock;
import dev.tauri.jsg.api.block.util.IItemBlock;
import dev.tauri.jsg.api.blockstates.JSGProperties;
import dev.tauri.jsg.api.item.ITabbedItem;
import dev.tauri.jsg.api.util.JSGAxisAlignedBB;
import dev.tauri.jsg.block.TickableBEBlock;
import dev.tauri.jsg.helpers.BlockPosHelper;
import dev.tauri.jsg.registry.TabRegistry;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class AbstractRingsCPBlock extends TickableBEBlock implements ITabbedItem, IHighlightBlock, IItemBlock, SimpleWaterloggedBlock {
    public static final Properties PANEL_PROPS = Properties.of()
            .explosionResistance(30f)
            .destroyTime(3f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.METAL);

    public AbstractRingsCPBlock() {
        super(PANEL_PROPS);
        this.registerDefaultState(
                defaultBlockState().setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, Direction.NORTH)
                .setValue(BlockStateProperties.WATERLOGGED, false)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.rotateDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), rotation));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, BlockPosHelper.flipDir(blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY), mirror));
    }

    @Override
    public List<RegistryObject<CreativeModeTab>> getTabs() {
        return List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_TRANSPORTATION);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        Player placer = ctx.getPlayer();
        if (placer == null) return defaultBlockState();
        var direction = ctx.getClickedFace();
        if (direction.getAxis() == Direction.Axis.Y) return null;
        if (!canAttachTo(ctx.getLevel(), ctx.getClickedPos().immutable().offset(direction.getOpposite().getNormal()), direction))
            return null;
        return defaultBlockState()
            .setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, direction)
            .setValue(BlockStateProperties.WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nonnull FluidState getFluidState(@Nonnull BlockState pState) {
        return pState.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;
        BlockEntity e = level.getBlockEntity(blockPos);
        if (e instanceof AbstractRingsCPBE be) {
            be.updateLinkStatus();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        super.playerWillDestroy(level, pos, blockState, player);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AbstractRingsCPBE cp) {
                cp.onBroken();
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AbstractRingsCPBE cp) {
                cp.onBroken();
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    private boolean canAttachTo(BlockGetter pBlockReader, BlockPos pPos, Direction pDirection) {
        BlockState blockstate = pBlockReader.getBlockState(pPos);
        return blockstate.isFaceSturdy(pBlockReader, pPos, pDirection);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        return this.canAttachTo(pLevel, pPos.relative(direction.getOpposite()), direction);
    }

    @Override
    public boolean renderHighlight(BlockState blockState) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        BlockPos min = new BlockPos(4, 0, 14);
        BlockPos max = new BlockPos(12, 16, 16);

        Direction horDir = blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);

        switch (horDir) {
            case SOUTH:
                min = new BlockPos(4, 0, 0);
                max = new BlockPos(12, 16, 2);
                break;
            case EAST:
                min = new BlockPos(0, 0, 4);
                max = new BlockPos(2, 16, 12);
                break;
            case WEST:
                min = new BlockPos(14, 0, 4);
                max = new BlockPos(16, 16, 12);
                break;
            default:
                break;
        }

        return Shapes.create(new JSGAxisAlignedBB(
                Math.min(Math.abs(min.getX() / 16D), Math.abs(max.getX() / 16D)), Math.min(Math.abs(min.getY() / 16D), Math.abs(max.getY() / 16D)), Math.min(Math.abs(min.getZ() / 16D), Math.abs(max.getZ() / 16D)),
                Math.max(Math.abs(min.getX() / 16D), Math.abs(max.getX() / 16D)), Math.max(Math.abs(min.getY() / 16D), Math.abs(max.getY() / 16D)), Math.max(Math.abs(min.getZ() / 16D), Math.abs(max.getZ() / 16D))
        ));
    }
}
