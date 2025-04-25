package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.item.JSGBlockItem;
import dev.tauri.jsg.item.JSGModelOBJInGUIRenderer;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsAncientCPBE;
import dev.tauri.jsgtransporters.common.item.ControllerItem;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsAncientCPBlock extends AbstractRingsCPBlock {
    public static final ResourceLocation SYMBOLS_TEX = new ResourceLocation(JSG.MOD_ID, "textures/tesr/rings/controller/ancient/button_0.png");

    public RingsAncientCPBlock() {
        super(Properties.of().noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsAncientCPBE(pPos, pState);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new ControllerItem(this) {
            @Override
            public JSGModelOBJInGUIRenderer.RenderPartInterface getRenderPartInterface() {
                return (itemStack, itemDisplayContext, stack, bufferSource, light, overlay) -> {
                    ModelsHolder.RINGS_CONTROLLER_ANCIENT_BASE.bindTextureAndRender(stack);

                    for (var symbol : SymbolAncientEnum.values()) {
                        stack.pushPose();
                        Constants.LOADERS_HOLDER.texture().getTexture(SYMBOLS_TEX).bindTexture();
                        Constants.LOADERS_HOLDER.model().getModel(symbol.modelResource).render(stack);
                        stack.popPose();
                    }
                };
            }
        };
    }
}
