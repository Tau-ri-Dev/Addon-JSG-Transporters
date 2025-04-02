package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.block.IHighlightBlock;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.util.JSGAxisAlignedBB;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RingsGoauldCPBlock extends AbstractRingsCPBlock implements IHighlightBlock {
    public RingsGoauldCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsGoauldCPBE(pPos, pState);
    }

    @Override
    public boolean renderHighlight(BlockState blockState) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
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
