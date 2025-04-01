package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldCPBE extends AbstractRingsCPBE {
    public RingsGoauldCPBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_CP_GOAULD_BE.get(), pPos, pBlockState);
    }
}
