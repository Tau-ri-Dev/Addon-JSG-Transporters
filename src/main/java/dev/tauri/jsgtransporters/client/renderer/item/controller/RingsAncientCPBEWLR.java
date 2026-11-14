package dev.tauri.jsgtransporters.client.renderer.item.controller;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.tauri.jsgcore.client.renderer.AbstractItemBEWLR;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RingsAncientCPBEWLR extends AbstractItemBEWLR {
    public static final ResourceLocation SYMBOLS_TEX = JSGMapping.rl(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/ancient/button_0.png");

    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay, float partialTick) {
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            stack.mulPose(Axis.ZP.rotationDegrees(-45));
            stack.translate(-0.25, 0.28, 0);
            stack.scale(2.4f, 2.4f, 2.4f);
        } else if (itemDisplayContext.firstPerson()) {
            stack.translate(0, 0.05, 0.7);
            stack.scale(1.5f, 1.5f, 1.5f);
        } else if (itemDisplayContext == ItemDisplayContext.GROUND) {
            stack.translate(0, 0.2, 0);
            stack.scale(2.5f, 2.5f, 2.5f);
        } else if (itemDisplayContext == ItemDisplayContext.FIXED) {
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.translate(-0.5, 0.55, -0.5);
            stack.scale(2.5f, 2.5f, 2.5f);
        }else {
            stack.translate(0, -0.3, 0.7);
            stack.scale(2.5f, 2.5f, 2.5f);
        }
        stack.mulPose(Axis.YP.rotationDegrees(180));
        ModelsHolder.RINGS_CONTROLLER_ANCIENT_BASE.bindTexture().render(stack, bufferSource, light);

        for (var symbol : SymbolAncientEnum.values()) {
            stack.pushPose();
            ClientConstants.LOADERS_HOLDER.texture().getTexture(SYMBOLS_TEX).bindTexture();
            ClientConstants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack, bufferSource, light);
            stack.popPose();
        }
    }
}
