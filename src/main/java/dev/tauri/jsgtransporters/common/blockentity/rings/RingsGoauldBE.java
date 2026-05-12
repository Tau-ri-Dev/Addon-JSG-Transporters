package dev.tauri.jsgtransporters.common.blockentity.rings;

import dev.tauri.jsg.core.common.symbol.SymbolType;
import dev.tauri.jsgtransporters.common.registry.JSGTBlockEntities;
import dev.tauri.jsgtransporters.common.registry.JSGTSymbolTypes;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldBE extends RingsAbstractBE {
    public RingsGoauldBE(BlockPos pPos, BlockState pBlockState) {
        super(JSGTBlockEntities.RINGS_GOAULD_BE.get(), pPos, pBlockState);
    }

    @Override
    public SymbolType<?> getSymbolType() {
        return JSGTSymbolTypes.GOAULD.get();
    }

    @Override
    public TagKey<Block> getControlPanelBlocks() {
        return JSGTBlockTags.RINGS_GOAULD_LINKABLE;
    }
}
