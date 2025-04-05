package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsgtransporters.common.blockentity.controller.RingsAncientCPBE;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsOriCPBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RingsAncientCPBlock extends AbstractRingsCPBlock {
    public RingsAncientCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsAncientCPBE(pPos, pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }
}
