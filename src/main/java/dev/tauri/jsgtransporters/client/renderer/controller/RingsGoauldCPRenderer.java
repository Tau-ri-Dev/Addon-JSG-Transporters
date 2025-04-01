package dev.tauri.jsgtransporters.client.renderer.controller;

import com.mojang.math.Axis;
import dev.tauri.jsg.config.JSGConfig;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import dev.tauri.jsgtransporters.common.raycaster.GoauldCPRaycaster;
import dev.tauri.jsgtransporters.common.state.renderer.RingsControlPanelRendererState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class RingsGoauldCPRenderer extends AbstractRingsCPRenderer<RingsControlPanelRendererState, RingsGoauldCPBE> {
    public RingsGoauldCPRenderer(BlockEntityRendererProvider.Context ignored) {
        super(ignored);
    }

    protected void translateToPos() {
        var rotation = level.getBlockState(tileEntity.getBlockPos()).getOptionalValue(JSGProperties.FACING_HORIZONTAL_PROPERTY).orElse(Direction.NORTH);
        switch (rotation) {
            case WEST:
                stack.translate(0, 0, 1);
                stack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case SOUTH:
                stack.translate(1, 0, 1);
                stack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                stack.translate(1, 0, 0);
                stack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            default:
                break;
        }
    }

    @Override
    protected void renderController() {
        translateToPos();
        ModelsHolder.RINGS_CONTROLLER_GOAULD.bindTextureAndRender(stack);
        ModelsHolder.RINGS_CONTROLLER_GOAULD_LIGHT.bindTextureAndRender(stack);

        if (JSGConfig.Debug.renderBoundingBoxes.get() || Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            this.stack.pushPose();
            for (var btn : GoauldCPRaycaster.BUTTONS) {
                btn.render(stack);
            }
            this.stack.popPose();
        }
    }
}
