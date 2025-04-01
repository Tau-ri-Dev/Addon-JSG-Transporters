package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsGoauldCPBlock extends AbstractRingsCPBlock {
    public RingsGoauldCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsGoauldCPBE(pPos, pState);
    }
}
