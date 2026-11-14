package dev.tauri.jsgtransporters.client.renderer.item.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.tauri.jsgcore.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgcore.mapping.JSGMapping;
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

    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, float partialTick) {
        stack.mulPose(Axis.YP.rotationDegrees(180));
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            stack.translate(0.03, 0.5, 0);
            stack.scale(1.7f, 1.7f, 1.7f);
        } else if (itemDisplayContext.firstPerson()) {
            stack.translate(0, -0.2, -0.6);
        } else if (itemDisplayContext == ItemDisplayContext.GROUND) {
            //stack.translate(-0.5, 0, -1);
            stack.scale(1.7f, 1.7f, 1.7f);
        } else if (itemDisplayContext == ItemDisplayContext.FIXED) {
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.translate(0.5, 0.6, 0.5);
            stack.scale(1.7f, 1.7f, 1.7f);
        } else {
            stack.translate(0, -0.6, -0.7);
            stack.scale(1.7f, 1.7f, 1.7f);
        }

        ModelsHolder.RINGS_CONTROLLER_ORI_BASE.bindTexture().render(stack, bufferSource, light);

        for (var symbol : SymbolOriEnum.values()) {
            stack.pushPose();
            ClientConstants.LOADERS_HOLDER.texture().getTexture(SYMBOLS_TEX).bindTexture();
            ClientConstants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, bufferSource, light);
            stack.popPose();
        }
    }
}
