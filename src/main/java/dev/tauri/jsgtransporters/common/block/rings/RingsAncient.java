package dev.tauri.jsgtransporters.common.block.rings;

import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAncientBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsAncient extends RingsClassicBlock {
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsAncientBE(pPos, pState);
    }
}
