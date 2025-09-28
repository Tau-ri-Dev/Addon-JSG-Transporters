package dev.tauri.jsgtransporters.client.renderer.controller;

import com.mojang.math.Axis;
import dev.tauri.jsg.config.JSGConfig;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import dev.tauri.jsgtransporters.common.raycaster.GoauldCPRaycaster;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import dev.tauri.jsgtransporters.common.state.renderer.RingsGoauldCPRendererState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import java.util.List;

public class RingsGoauldCPRenderer extends AbstractRingsCPRenderer<RingsGoauldCPRendererState, RingsGoauldCPBE> {
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

        //.Constants.LOADERS_HOLDER.texture().getTexture(rendererState.getButtonTexture(SymbolGoauldEnum.LIGHT, rendererState.getBiomeOverlay())).bindTexture();
        //ModelsHolder.RINGS_CONTROLLER_GOAULD_LIGHT.render(stack, rendererState.isButtonActive(SymbolGoauldEnum.LIGHT));

        for (var symbol : SymbolGoauldEnum.values()) {
            //if (symbol.brb()) continue;
            stack.pushPose();
            var state = rendererState.getActualButtonState(symbol) / 15f;
            if (!symbol.brb())
                stack.translate(0, 0, 0.01f * (state + 1.8f));
            var tex = rendererState.getButtonTexture(symbol, rendererState.getBiomeOverlay());
            Constants.LOADERS_HOLDER.texture().getTexture(tex).bindTexture();
            Constants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, rendererState.isButtonActive(symbol));
            stack.popPose();
        }
    }

    @Override
    public List<RayCastedButton> getRaycasterButtons() {
        return GoauldCPRaycaster.BUTTONS;
    }

    @Override
    public Raycaster getRaycaster() {
        return GoauldCPRaycaster.INSTANCE;
    }
}
