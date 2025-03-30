package dev.tauri.jsgtransporters.client.renderer;

import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsGoauldBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RingsGoauldRenderer extends RingsAbstractRenderer<RingsRendererState, RingsGoauldBE> {
    public RingsGoauldRenderer(BlockEntityRendererProvider.Context ignored) {
        super(ignored);
    }

    @Override
    public void renderRing(int index) {
        var size = 0.48f;
        stack.scale(size, size, size);
        ModelsHolder.RING_GOAULD.bindTextureAndRender(stack);
    }
}
