package dev.tauri.jsgtransporters.client.renderer.rings;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.tauri.jsg.loader.model.OBJModel;
import dev.tauri.jsg.renderer.LinkableRenderer;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE.RING_ANIMATION_LENGTH;

public abstract class RingsAbstractRenderer<S extends RingsRendererState, T extends RingsAbstractBE> implements BlockEntityRenderer<T>, LinkableRenderer {
    public RingsAbstractRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    public T tileEntity;
    public PoseStack stack;
    public MultiBufferSource source;
    public int combinedLight;
    public Level level;
    public float partialTicks;
    public S rendererState;

    @Override
    @ParametersAreNonnullByDefault
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
        if (getStartingOffset() < 0) index = getRingsCount() - index - 1;

        var startX = 0.25f * index;
        var endX = 5f - (0.25f * index);

        if (x < startX || x > (endX + 1f)) return 0f;
        if (x <= (startX + 1)) return START_FUNC.apply(x - startX);
        if (x >= endX) return END_FUNC.apply(x - endX);
        return 1f;
    };

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
        this.combinedLight = getCombinedLight(0);
        OBJModel.source = this.source;
        OBJModel.packedLight = this.combinedLight;
        OBJModel.resetRGB();
        OBJModel.resetDynamicLightning();

        renderLink(tileEntity.getBlockPos(), tileEntity, pPoseStack);

        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);
        stack.pushPose();
        RenderSystem.enableDepthTest();

        var ringsPoints = new HashMap<Integer, Pair<Double, Double>>();

        for (var i = 0; i < getRingsCount(); i++) {
            var y = getYOffset(i);
            if (y == 0 && i != (getStartingOffset() >= 0 ? (getRingsCount() - 1) : 0)) continue;
            stack.pushPose();
            stack.translate(0, y, 0);
            OBJModel.packedLight = getCombinedLight((int) Math.ceil(y));
            renderRing(i);
            OBJModel.packedLight = combinedLight;
            stack.popPose();
            ringsPoints.put(i, getRingCorners(i));
        }

        var whiteFlashY = renderWhiteFlash();
        if (whiteFlashY != null) {
            var diameter = getRingDiameter();
            for (var i = 0; i < (getRingsCount() - 1); i++) {
                var current = ringsPoints.get(i);
                var next = ringsPoints.get(i + 1);

                var top = new Vector2f(diameter / 2f, current.right().floatValue());
                var bottom = new Vector2f(diameter / 2f, next.left().floatValue());

                drawWhiteFlashOutstandingQuad(whiteFlashY, top, bottom);
            }
        }

        stack.popPose();
        stack.popPose();
        RenderSystem.disableDepthTest();
    }


    public abstract void renderRing(int index);

    public int getRingsCount() {
        return 5;
    }

    public float getRingDiameter() {
        return 4.6f;
    }

    public double getYOffset(int index) {
        var time = rendererState.getAnimationTick(level.getGameTime(), partialTicks);
        var coef = (float) (time / (double) RING_ANIMATION_LENGTH);
        var value = RING_ANIMATION.apply(coef, index);
        return value * ((getStartingOffset() - 1f) + (3f - ((3f / getRingsCount()) * index) + 0.25f));
    }

    public Pair<Double, Double> getRingCorners(int index) {
        var middle = ((getStartingOffset() - 1f) + (3f - ((3f / getRingsCount()) * index) + 0.25f));
        var bottom = middle - (getRingHeight() / 2f);
        var top = middle + (getRingHeight() / 2f);
        return Pair.of(top, bottom);
    }

    public float getRingHeight() {
        return 0.25f;
    }

    // should be ranged (>= 1 || <= -4) -> if not rings will overlap in the animation
    public double getStartingOffset() {
        return tileEntity.getVerticalOffset();
    }

    @Nullable
    public Double renderWhiteFlash() {
        var time = (rendererState.getAnimationTick(level.getGameTime(), partialTicks) - ((3.834 - 1.674) * 20));
        if (time < 0) return null;
        var coef = (float) (time / ((4.96 - 4.0) * 20));
        if (coef > 1.1f) return null;
        var y = getStartingOffset() + (getStartingOffset() < 0 ? (coef * 3f) : (3f - (coef * 3f))) - 1f;


        List<Pair<Double, Pair<Vector2f, Vector2f>>> mapped = new ArrayList<>();
        if (Minecraft.getInstance().player == null) return null;
        var pPos = Minecraft.getInstance().player.position();
        for (var v : WHITE_FLASH_VERTEXES) {
            var pos = tileEntity.getBlockPos().getCenter().add(v.first().getX(), 0, v.first().getY());
            var dist = pPos.distanceTo(pos);
            mapped.add(Pair.of(dist, v));
        }
        var sorted = mapped.stream().sorted((e1, e2) -> e2.first().compareTo(e1.first())).collect(Collectors.toCollection(LinkedHashSet::new));

        stack.pushPose();
        stack.translate(0, y - 0.25f, 0);
        stack.scale(1.65f, 1.5f, 1.65f);

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        for (var quad : sorted) {
            drawWhiteFlashQuad(quad.second().first(), quad.second().second());
        }
        RenderSystem.disableBlend();


        stack.popPose();
        return y - 0.5;
    }

    private static final LinkedList<Pair<Vector2f, Vector2f>> WHITE_FLASH_VERTEXES = new LinkedList<>();

    static {
        var step = (360f / 36f);
        Vector2f lastVec = null;
        for (var i = 0f; i <= 360f; i += step) {
            var angle = Math.toRadians(i);
            var x = (float) Math.cos(angle);
            var z = (float) Math.sin(angle);
            if (lastVec != null)
                WHITE_FLASH_VERTEXES.addLast(Pair.of(lastVec, new Vector2f(x, z)));
            lastVec = new Vector2f(x, z);
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

    private void drawWhiteFlashOutstandingQuad(double y, Vector2f ringPointTop, Vector2f ringPointBottom) {
        var p = Minecraft.getInstance().player;
        if (p == null) return;
        var coef = 1f - (float) (Math.abs(ringPointBottom.getY() - y) / ringPointBottom.getX()) / 2f;
        if (coef < 0.5f) return;
        var length = 2f * coef;

        stack.pushPose();
        var playerRot = p.getViewYRot(partialTicks);
        stack.mulPose(Axis.YN.rotationDegrees(playerRot));
        for (int i = 0; i < 2; i++) {
            stack.pushPose();
            if (i == 1)
                stack.mulPose(Axis.YP.rotationDegrees(180));
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            var t = Tesselator.getInstance();
            var b = t.getBuilder();
            var matrix = stack.last().pose();
            b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            // between rings
            b.vertex(matrix, ringPointBottom.getX() - 1f, ringPointBottom.getY(), 0).color(1f, 1f, 1f, 1f).endVertex();
            b.vertex(matrix, ringPointTop.getX() - 1f, ringPointTop.getY(), 0).color(1f, 1f, 1f, 1f).endVertex();
            b.vertex(matrix, ringPointTop.getX() - (1 - Math.min(1, coef * 1.5f)), ringPointTop.getY(), 0).color(1f, 1f, 1f, Math.min(1, coef * 1.5f)).endVertex();
            b.vertex(matrix, ringPointBottom.getX() - (1 - Math.min(1, coef * 1.5f)), ringPointBottom.getY(), 0).color(1f, 1f, 1f, Math.min(1, coef * 1.5f)).endVertex();

            // closer to center
            b.vertex(matrix, ringPointBottom.getX(), ringPointBottom.getY(), 0).color(1f, 1f, 1f, 1f).endVertex();
            b.vertex(matrix, ringPointTop.getX(), ringPointTop.getY(), 0).color(1f, 1f, 1f, 1f).endVertex();

            // outshining
            var ringPointBottomExt = new Vector2f(ringPointBottom.getX() * length, (float) (ringPointBottom.getY() + (ringPointBottom.getY() - y) * (coef * 1.5f - 0.5f)));
            var ringPointTopExt = new Vector2f(ringPointTop.getX() * length, (float) (ringPointTop.getY() + (ringPointTop.getY() - y) * (coef * 1.5f - 0.5f)));
            b.vertex(matrix, ringPointTopExt.getX(), ringPointTopExt.getY(), 0).color(1f, 1f, 1f, 0f).endVertex();
            b.vertex(matrix, ringPointBottomExt.getX(), ringPointBottomExt.getY(), 0).color(1f, 1f, 1f, 0f).endVertex();

            BufferUploader.drawWithShader(b.end());
            RenderSystem.disableBlend();
            stack.popPose();
        }
        stack.popPose();
    }
}
