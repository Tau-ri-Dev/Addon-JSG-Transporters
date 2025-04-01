package dev.tauri.jsgtransporters.client.renderer;

import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAncientBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RingsAncientRenderer extends RingsAbstractRenderer<RingsRendererState, RingsAncientBE> {
    public RingsAncientRenderer(BlockEntityRendererProvider.Context ignored) {
        super(ignored);
    }

    @Override
    public void renderRing(int index) {
        var size = 0.48f;
        stack.scale(size, size, size);
        ModelsHolder.RING_ANCIENT.bindTextureAndRender(stack);
    }
}
