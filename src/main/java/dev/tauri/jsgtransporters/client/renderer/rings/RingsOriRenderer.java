package dev.tauri.jsgtransporters.client.renderer.rings;

import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsOriBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RingsOriRenderer extends RingsAbstractRenderer<RingsRendererState, RingsOriBE> {
    public RingsOriRenderer(BlockEntityRendererProvider.Context ignored) {
        super(ignored);
    }

    @Override
    public void renderRing(int index, int light) {
        var size = 0.48f;
        stack.scale(size, size, size);
        ModelsHolder.RING_ORI.bindTexture().render(stack, source, light);
    }
}
