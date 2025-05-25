package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.state.renderer.RingsOriCPRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsOriCPBE extends AbstractRingsCPBE {
    public RingsOriCPBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_CP_ORI_BE.get(), pPos, pBlockState);
    }

    protected RingsOriCPRendererState rendererState = new RingsOriCPRendererState();

    @Override
    public RingsOriCPRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public TagKey<Block> getRingsBlocks() {
        return TagsRegistry.PANEL_ORI_LINKABLE;
    }
}
