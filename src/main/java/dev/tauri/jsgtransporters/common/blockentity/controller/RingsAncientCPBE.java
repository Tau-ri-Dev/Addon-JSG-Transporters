package dev.tauri.jsgtransporters.common.blockentity.controller;

import dev.tauri.jsgtransporters.common.registry.BlockEntityRegistry;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.state.renderer.RingsAncientCPRendererState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RingsAncientCPBE extends AbstractRingsCPBE {
    public RingsAncientCPBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.RINGS_CP_ANCIENT_BE.get(), pPos, pBlockState);
    }

    protected RingsAncientCPRendererState rendererState = new RingsAncientCPRendererState();

    @Override

    public RingsAncientCPRendererState getRendererStateClient() {
        return rendererState;
    }

    @Override
    public TagKey<Block> getRingsBlocks() {
        return TagsRegistry.PANEL_ANCIENT_LINKABLE;
    }
}
