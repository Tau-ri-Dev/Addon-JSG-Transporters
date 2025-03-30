package dev.tauri.jsgtransporters.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tauri.jsg.loader.model.OBJModel;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

public abstract class RingsAbstractRenderer<S extends RingsRendererState, T extends RingsAbstractBE> implements BlockEntityRenderer<T> {
    public RingsAbstractRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    public RingsAbstractBE tileEntity;
    public PoseStack stack;
    public MultiBufferSource source;
    public int combinedLight;
    public Level level;
    public float partialTicks;
    public S rendererState;

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 64 * 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        tileEntity = pBlockEntity;
        rendererState = (S) pBlockEntity.getRendererStateClient();
        this.stack = pPoseStack;
        this.partialTicks = pPartialTick;
        this.source = pBuffer;
        level = tileEntity.getLevel();
        if (level == null) return;
        this.combinedLight = pPackedLight;
        OBJModel.source = this.source;
        OBJModel.packedLight = this.combinedLight;
        OBJModel.resetRGB();
        OBJModel.resetDynamicLightning();
    }


    public abstract void renderRing();
}
