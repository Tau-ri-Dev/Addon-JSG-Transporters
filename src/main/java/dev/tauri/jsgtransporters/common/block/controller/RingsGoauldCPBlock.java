package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.item.JSGBlockItem;
import dev.tauri.jsg.item.JSGModelOBJInGUIRenderer;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import dev.tauri.jsgtransporters.common.item.ControllerItem;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsGoauldCPBlock extends AbstractRingsCPBlock {
    public static final ResourceLocation SYMBOLS_TEX = new ResourceLocation(JSG.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_button_0.jpg");
    public static final ResourceLocation LIGHT_TEX = new ResourceLocation(JSG.MOD_ID, "textures/tesr/rings/controller/goauld/goauld_light_0.jpg");

    public RingsGoauldCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsGoauldCPBE(pPos, pState);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new ControllerItem(this) {
            @Override
            public JSGModelOBJInGUIRenderer.RenderPartInterface getRenderPartInterface() {
                return (itemStack, itemDisplayContext, stack, bufferSource, light, overlay) -> {
                    ModelsHolder.RINGS_CONTROLLER_GOAULD.bindTextureAndRender(stack);

                    for (var symbol : SymbolGoauldEnum.values()) {
                        stack.pushPose();
                        Constants.LOADERS_HOLDER.texture().getTexture(symbol.brb() ? LIGHT_TEX : SYMBOLS_TEX).bindTexture();
                        Constants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack);
                        stack.popPose();
                    }
                };
            }
        };
    }
}
