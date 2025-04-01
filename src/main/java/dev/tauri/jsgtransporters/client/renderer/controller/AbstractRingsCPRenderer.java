package dev.tauri.jsgtransporters.client.renderer.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tauri.jsg.loader.model.OBJModel;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsControlPanelRendererState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractRingsCPRenderer<S extends RingsControlPanelRendererState, T extends AbstractRingsCPBE> implements BlockEntityRenderer<T> {
    public AbstractRingsCPRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    public T tileEntity;
    public PoseStack stack;
    public MultiBufferSource source;
    public int combinedLight;
    public Level level;
    public float partialTicks;
    public S rendererState;

    @Override
    public int getViewDistance() {
        return 64 * 3;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    protected abstract void renderController();

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("unchecked")
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        tileEntity = pBlockEntity;
        if (tileEntity.getRendererStateClient() == null) return;
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

        stack.pushPose();
        renderController();
        stack.popPose();
    }
}
