package dev.tauri.jsgtransporters.client.renderer.item.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.tauri.jsgcore.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RingsGoauldCPBEWLR extends AbstractItemBEWLR {
    public static final ResourceLocation SYMBOLS_TEX = JSGMapping.rl(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_button_0.jpg");
    public static final ResourceLocation LIGHT_TEX = JSGMapping.rl(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_light_0.jpg");

    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, float partialTick) {
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            stack.mulPose(Axis.ZP.rotationDegrees(-45));
            stack.translate(-0.75, -0.25, 0);
        } else if (itemDisplayContext.firstPerson()) {
            stack.translate(-0.5, -0.8, -0.2);
        } else if (itemDisplayContext == ItemDisplayContext.GROUND) {
            stack.translate(-0.5, 0, -1);
        } else if (itemDisplayContext == ItemDisplayContext.FIXED) {
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.translate(-1, 0, -1.5);
        } else {
            stack.translate(-0.5, -0.9, -0.2);
        }
        ModelsHolder.RINGS_CONTROLLER_GOAULD.bindTexture().render(stack, bufferSource, light);

        for (var symbol : SymbolGoauldEnum.values()) {
            stack.pushPose();
            ClientConstants.LOADERS_HOLDER.texture().getTexture(symbol.brb() ? LIGHT_TEX : SYMBOLS_TEX).bindTexture();
            ClientConstants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, bufferSource, light);
            stack.popPose();
        }
    }

    @Override
    public boolean renderHands(ItemDisplayContext itemDisplayContext) {
        return false;
    }
}
