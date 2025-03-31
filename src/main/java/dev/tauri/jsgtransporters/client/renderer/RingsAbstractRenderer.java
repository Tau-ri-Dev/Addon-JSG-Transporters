package dev.tauri.jsgtransporters.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.tauri.jsg.command.commands.CommandTest;
import dev.tauri.jsg.loader.model.OBJModel;
import dev.tauri.jsg.util.math.MathFunctionImpl;
import dev.tauri.jsg.util.vectors.Vector2f;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.state.renderer.RingsRendererState;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

    private final MathFunctionImpl START_FUNC = new MathFunctionImpl((x) -> {
        var sin = Math.sin(x * Math.PI);
        var c = 25f;
        return (float) ((-Math.cos(x * Math.PI) + ((sin * sin) / (1.035f * (1 + (Math.abs(getStartingOffset() - 1f) / c)))) + Math.sin(x * Math.PI * 2) / (5f * (1 + (Math.abs(getStartingOffset() - 1f) / c) * 5f)) + 1f) / 2f);
    });
    private static final MathFunctionImpl END_FUNC = new MathFunctionImpl((x) -> (float) ((-Math.cos((x + 1) * Math.PI) + 1f) / 2f));

    public final BiFunction<Float, Integer, Float> RING_ANIMATION = (x, index) -> {
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

        renderWhiteFlash();

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
        return value * ((getStartingOffset() - 1f) + (3f - ((3f / getRingsCount()) * index) + 0.25f));
    }

    public double getStartingOffset() {
        return CommandTest.y + 1;
    }

    public void renderWhiteFlash() {
        var time = (rendererState.getAnimationTick(level.getGameTime(), partialTicks) - ((3.834 - 1.674) * 20));
        if (time < 0) return;
        var coef = (float) (time / ((4.96 - 3.834) * 20));
        if (coef > 1.1f) return;
        var y = getStartingOffset() + (coef * 3f) - 1f;


        List<Pair<Double, Pair<Vector2f, Vector2f>>> mapped = new ArrayList<>();
        if (Minecraft.getInstance().player == null) return;
        var pPos = Minecraft.getInstance().player.position();
        for (var v : WHITE_FLASH_VERTEXES) {
            var pos = tileEntity.getBlockPos().getCenter().add(v.first().getX(), 0, v.first().getY());
            var dist = pPos.distanceTo(pos);
            mapped.add(Pair.of(dist, v));
        }
        var sorted = mapped.stream().sorted((e1, e2) -> e2.first().compareTo(e1.first())).collect(Collectors.toCollection(LinkedHashSet::new));

        stack.pushPose();
        stack.translate(0, y, 0);
        stack.scale(1.65f, 1, 1.65f);

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        for (var quad : sorted) {
            drawWhiteFlashQuad(quad.second().first(), quad.second().second());
        }
        RenderSystem.disableBlend();


        stack.popPose();
    }

    private static final LinkedList<Pair<Vector2f, Vector2f>> WHITE_FLASH_VERTEXES = new LinkedList<>();

    static {
        var step = (360f / 36f);
        Vector2f lastVec = null;
        for (var i = 0f; i <= 360f; i += step) {
            var angle = Math.toRadians(i);
            var x = (float) Math.cos(angle);
            var z = (float) Math.sin(angle);
            if (lastVec == null) lastVec = new Vector2f(x, z);
            else {
                WHITE_FLASH_VERTEXES.addLast(Pair.of(lastVec, new Vector2f(x, z)));
                lastVec = new Vector2f(x, z);
            }
        }
    }

    private void drawWhiteFlashQuad(Vector2f start, Vector2f end) {
        stack.pushPose();
        var t = Tesselator.getInstance();
        var b = t.getBuilder();
        var matrix = stack.last().pose();
        b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // bottom
        b.vertex(matrix, start.x, -0.75f, start.y).color(1f, 1f, 1f, 0f).endVertex();
        b.vertex(matrix, end.x, -0.75f, end.y).color(1f, 1f, 1f, 0f).endVertex();
        b.vertex(matrix, end.x, 1 / 3f - 0.5f, end.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, start.x, 1 / 3f - 0.5f, start.y).color(1f, 1f, 1f, 1f).endVertex();

        // middle
        b.vertex(matrix, start.x, 1 / 3f - 0.5f, start.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, end.x, 1 / 3f - 0.5f, end.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, end.x, 2 / 3f - 0.5f, end.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, start.x, 2 / 3f - 0.5f, start.y).color(1f, 1f, 1f, 1f).endVertex();

        // top
        b.vertex(matrix, start.x, 2 / 3f - 0.5f, start.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, end.x, 2 / 3f - 0.5f, end.y).color(1f, 1f, 1f, 1f).endVertex();
        b.vertex(matrix, end.x, 0.75f, end.y).color(1f, 1f, 1f, 0f).endVertex();
        b.vertex(matrix, start.x, 0.75f, start.y).color(1f, 1f, 1f, 0f).endVertex();

        BufferUploader.drawWithShader(b.end());
        stack.popPose();
    }
}
