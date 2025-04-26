package dev.tauri.jsgtransporters.client.renderer.controller;

import com.mojang.math.Axis;
import dev.tauri.jsg.command.commands.CommandTest;
import dev.tauri.jsg.config.JSGConfig;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsAncientCPBE;
import dev.tauri.jsgtransporters.common.raycaster.AncientCPRaycaster;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import dev.tauri.jsgtransporters.common.state.renderer.RingsAncientCPRendererState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class RingsAncientCPRenderer extends AbstractRingsCPRenderer<RingsAncientCPRendererState, RingsAncientCPBE> {
    public RingsAncientCPRenderer(BlockEntityRendererProvider.Context ignored) {
        super(ignored);
    }

    protected void translateToPos() {
        var rotation = level.getBlockState(tileEntity.getBlockPos()).getOptionalValue(JSGProperties.FACING_HORIZONTAL_PROPERTY).orElse(Direction.NORTH);
        switch (rotation) {
            case WEST:
                stack.translate(0.97, 0.5, 0.51);
                stack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case SOUTH:
                stack.translate(0.5, 0.5, 0.03);
                break;
            case NORTH:
                stack.translate(0.5, 0.5, 0.97);
                stack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                stack.translate(0.03, 0.5, 0.49);
                stack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            default:
                break;
        }
    }

    @Override
    protected void renderController() {
        translateToPos();
        stack.scale(2, 2, 2);

        if (JSGConfig.Debug.renderBoundingBoxes.get() || Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            this.stack.pushPose();
            for (var btn : AncientCPRaycaster.BUTTONS) {
                btn.render(stack);
            }
            this.stack.popPose();
        }

        ModelsHolder.RINGS_CONTROLLER_ANCIENT_BASE.bindTextureAndRender(stack);


        for (var symbol : SymbolAncientEnum.values()) {
            stack.pushPose();
            var state = rendererState.getActualButtonState(symbol) / 15f;
            stack.translate(0, 0, -0.008f * state);
            var tex = rendererState.getButtonTexture(symbol, rendererState.getBiomeOverlay());
            Constants.LOADERS_HOLDER.texture().getTexture(tex).bindTexture();
            Constants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, rendererState.isButtonActive(symbol));
            stack.popPose();
        }
    }
}
