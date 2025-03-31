package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldBE extends RingsClassicBE {
    public RingsGoauldBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_GOAULD_BE.get(), pPos, pBlockState);
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return SymbolTypeRegistry.GOAULD;
    }
}
