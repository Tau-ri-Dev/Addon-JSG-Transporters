package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.registry.BlockRegistry;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldBE extends RingsAbstractBE {
    public RingsGoauldBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_GOAULD_BE.get(), pPos, pBlockState);
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return SymbolTypeRegistry.GOAULD;
    }

    @Override
    public Block getControlPanelBlock() {
        return BlockRegistry.RINGS_CP_GOAULD.get();
    }
}
