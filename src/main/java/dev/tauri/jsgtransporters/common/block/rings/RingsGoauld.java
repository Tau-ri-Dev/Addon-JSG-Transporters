package dev.tauri.jsgtransporters.common.block.rings;

import dev.tauri.jsgtransporters.common.blockentity.rings.RingsGoauldBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsGoauld extends RingsClassicBlock {
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsGoauldBE(pPos, pState);
    }
}
