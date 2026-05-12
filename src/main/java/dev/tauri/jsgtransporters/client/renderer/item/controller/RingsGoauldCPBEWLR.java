package dev.tauri.jsgtransporters.client.renderer.item.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tauri.jsg.core.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RingsGoauldCPBEWLR extends AbstractItemBEWLR {
    public static final ResourceLocation SYMBOLS_TEX = new ResourceLocation(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_button_0.jpg");
    public static final ResourceLocation LIGHT_TEX = new ResourceLocation(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_light_0.jpg");

    // TODO(Mine): Fix transforms
    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, float partialTick) {
        stack.translate(-0.9, -0.4, 0);
        stack.scale(1.8f, 1.8f, 1.8f);
        ModelsHolder.RINGS_CONTROLLER_GOAULD.bindTexture().render(stack, bufferSource, light);

        for (var symbol : SymbolGoauldEnum.values()) {
            stack.pushPose();
            ClientConstants.LOADERS_HOLDER.texture().getTexture(symbol.brb() ? LIGHT_TEX : SYMBOLS_TEX).bindTexture();
            ClientConstants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, bufferSource, light);
            stack.popPose();
        }
    }
}
