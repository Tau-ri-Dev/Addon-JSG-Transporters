package dev.tauri.jsgtransporters.client.renderer.item.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.tauri.jsg.core.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.rings.network.SymbolOriEnum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RingsOriCPBEWLR extends AbstractItemBEWLR {
    public static final ResourceLocation SYMBOLS_TEX = JSGMapping.rl(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/ori/button_0.png");

    // TODO(Mine): Fix transforms
    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, float partialTick) {
        stack.translate(0, 0.5, 0);
        stack.scale(3, 3, 3);
        stack.mulPose(Axis.YP.rotationDegrees(180));
        ModelsHolder.RINGS_CONTROLLER_ORI_BASE.bindTexture().render(stack, bufferSource, light);

        for (var symbol : SymbolOriEnum.values()) {
            stack.pushPose();
            ClientConstants.LOADERS_HOLDER.texture().getTexture(SYMBOLS_TEX).bindTexture();
            ClientConstants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, bufferSource, light);
            stack.popPose();
        }
    }
}
