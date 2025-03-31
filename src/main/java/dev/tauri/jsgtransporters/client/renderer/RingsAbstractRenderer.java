package dev.tauri.jsgtransporters.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.tauri.jsg.loader.model.OBJModel;
import dev.tauri.jsg.util.math.MathFunctionImpl;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

import static dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE.RING_ANIMATION_LENGTH;

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

    public int getCombinedLight(int yOffset) {
        int count = 0;
        int count2 = 0;
        long blockSum = 0;
        long skySum = 0;
        for (var x = -2; x <= 2; x++) {
            for (var z = -2; z <= 2; z += (x == -2 || x == 2 ? 1 : 4)) {
                var pos = tileEntity.getBlockPos().offset(x, yOffset, z);
                count++;
                count2 += 2;
                int light = LevelRenderer.getLightColor(level, pos);
                blockSum += (LightTexture.block(light) * 2L);
                skySum += LightTexture.sky(light);
            }
        }
        if (count == 0) return LightTexture.FULL_BRIGHT;
        return LightTexture.pack((int) (blockSum / count2), (int) (skySum / count));
    }

    private static final MathFunctionImpl START_FUNC = new MathFunctionImpl((x) -> {
        var sin = Math.sin(x * Math.PI);
        return (float) ((-Math.cos(x * Math.PI) + (sin * sin) / 1.035f + Math.sin(x * Math.PI * 2) / 5f + 1f) / 2f);
    });
    private static final MathFunctionImpl END_FUNC = new MathFunctionImpl((x) -> {
        var sin = Math.sin(x * Math.PI);
        return (float) ((-Math.cos((x + 1) * Math.PI) - (sin * sin) / 1.035f - Math.sin(x * Math.PI * 2) / 5f + 1f) / 2f);
    });

    public static final BiFunction<Float, Integer, Float> RING_ANIMATION = (x, index) -> {
        x = x * 6;

        var startX = 0.25f * index;
        var endX = 5f - (0.25f * index);

        if (x < startX || x > (endX + 1f)) return 0f;
        if (x <= (startX + 1)) return START_FUNC.apply(x - startX);
        if (x >= endX) return END_FUNC.apply(x - endX);
        return 1f;
    };

    @Override
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
        this.combinedLight = getCombinedLight(0);
        OBJModel.source = this.source;
        OBJModel.packedLight = this.combinedLight;
        OBJModel.resetRGB();
        OBJModel.resetDynamicLightning();

        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);
        stack.pushPose();
        RenderSystem.enableDepthTest();

        for (var i = 0; i < getRingsCount(); i++) {
            var y = getYOffset(i);
            if (y == 0 && i != (getRingsCount() - 1)) continue;
            stack.pushPose();
            stack.translate(0, y, 0);
            OBJModel.packedLight = getCombinedLight((int) Math.ceil(y));
            renderRing(i);
            OBJModel.packedLight = combinedLight;
            stack.popPose();
        }

        stack.popPose();
        stack.popPose();
        RenderSystem.disableDepthTest();
    }


    public abstract void renderRing(int index);

    public int getRingsCount() {
        return 5;
    }

    public double getYOffset(int index) {
        var time = rendererState.getAnimationTick(level.getGameTime(), partialTicks);
        var coef = (float) (time / (double) RING_ANIMATION_LENGTH);
        var value = RING_ANIMATION.apply(coef, index);
        return value * (3f - ((3f / getRingsCount()) * index) + 0.25f);
    }
}
