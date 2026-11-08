package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.JSGTBlockEntities;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import dev.tauri.jsgtransporters.common.state.renderer.RingsGoauldCPRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldCPBE extends AbstractRingsCPBE {
    public RingsGoauldCPBE(BlockPos pPos, BlockState pBlockState) {
        super(JSGTBlockEntities.RINGS_CP_GOAULD_BE.get(), pPos, pBlockState);
    }

    protected RingsGoauldCPRendererState rendererState = new RingsGoauldCPRendererState();

    @Override
    public RingsGoauldCPRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public TagKey<Block> getRingsBlocks() {
        return JSGTBlockTags.PANEL_GOAULD_LINKABLE;
    }
}
