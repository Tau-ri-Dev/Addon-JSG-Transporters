package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RingsOriBE extends RingsClassicBE {
    public RingsOriBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_ORI_BE.get(), pPos, pBlockState);
    }
}
