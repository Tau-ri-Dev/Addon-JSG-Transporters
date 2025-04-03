package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.util.JSGAxisAlignedBB;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsOriCPBE;
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

public class RingsOriCPBlock extends AbstractRingsCPBlock {
    public RingsOriCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsOriCPBE(pPos, pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        BlockPos min = new BlockPos(0, 2, 14);
        BlockPos max = new BlockPos(16, 14, 16);

        Direction horDir = blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);

        switch (horDir) {
            case SOUTH:
                min = new BlockPos(0, 2, 0);
                max = new BlockPos(16, 14, 2);
                break;
            case EAST:
                min = new BlockPos(0, 2, 0);
                max = new BlockPos(2, 14, 16);
                break;
            case WEST:
                min = new BlockPos(14, 2, 0);
                max = new BlockPos(16, 14, 16);
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
