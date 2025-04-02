package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.rings.network.SymbolTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsAncientBE extends RingsClassicBE {
    public RingsAncientBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_ANCIENT_BE.get(), pPos, pBlockState);
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return SymbolTypeRegistry.ANCIENT;
    }

    @Override
    public Block getControlPanelBlock() {
        return null;
    }
}
