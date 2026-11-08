package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.JSGTBlockEntities;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import dev.tauri.jsgtransporters.common.state.renderer.RingsOriCPRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsOriCPBE extends AbstractRingsCPBE {
    public RingsOriCPBE(BlockPos pPos, BlockState pBlockState) {
        super(JSGTBlockEntities.RINGS_CP_ORI_BE.get(), pPos, pBlockState);
    }

    protected RingsOriCPRendererState rendererState = new RingsOriCPRendererState();

    @Override
    public RingsOriCPRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public TagKey<Block> getRingsBlocks() {
        return JSGTBlockTags.PANEL_ORI_LINKABLE;
    }
}
