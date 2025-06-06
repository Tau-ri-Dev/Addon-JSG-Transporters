package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.state.renderer.RingsGoauldCPRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsGoauldCPBE extends AbstractRingsCPBE {
    public RingsGoauldCPBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_CP_GOAULD_BE.get(), pPos, pBlockState);
    }

    protected RingsGoauldCPRendererState rendererState = new RingsGoauldCPRendererState();

    @Override
    public RingsGoauldCPRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public TagKey<Block> getRingsBlocks() {
        return TagsRegistry.PANEL_GOAULD_LINKABLE;
    }
}
