package dev.tauri.jsgtransporters.common.block.controller;

import com.mojang.math.Axis;
import dev.tauri.jsg.item.JSGBlockItem;
import dev.tauri.jsg.item.JSGModelOBJInGUIRenderer;
import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.util.JSGAxisAlignedBB;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ModelsHolder;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsOriCPBE;
import dev.tauri.jsgtransporters.common.item.ControllerItem;
import dev.tauri.jsgtransporters.common.rings.network.SymbolOriEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RingsOriCPBlock extends AbstractRingsCPBlock {
    public static final ResourceLocation SYMBOLS_TEX = new ResourceLocation(JSGTransporters.MOD_ID, "textures/tesr/rings/controller/ori/button_0.png");

    public RingsOriCPBlock() {
        super();
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsOriCPBE(pPos, pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        BlockPos min = new BlockPos(0, 2, 14);
        BlockPos max = new BlockPos(16, 14, 16);

        Direction horDir = blockState.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);

        switch (horDir) {
            case SOUTH:
                min = new BlockPos(0, 2, 0);
                max = new BlockPos(16, 14, 2);
                break;
            case EAST:
                min = new BlockPos(0, 2, 0);
                max = new BlockPos(2, 14, 16);
                break;
            case WEST:
                min = new BlockPos(14, 2, 0);
                max = new BlockPos(16, 14, 16);
                break;
            default:
                break;
        }

        return Shapes.create(new JSGAxisAlignedBB(
                Math.min(Math.abs(min.getX() / 16D), Math.abs(max.getX() / 16D)), Math.min(Math.abs(min.getY() / 16D), Math.abs(max.getY() / 16D)), Math.min(Math.abs(min.getZ() / 16D), Math.abs(max.getZ() / 16D)),
                Math.max(Math.abs(min.getX() / 16D), Math.abs(max.getX() / 16D)), Math.max(Math.abs(min.getY() / 16D), Math.abs(max.getY() / 16D)), Math.max(Math.abs(min.getZ() / 16D), Math.abs(max.getZ() / 16D))
        ));
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new ControllerItem(this) {
            @Override
            public JSGModelOBJInGUIRenderer.RenderPartInterface getRenderPartInterface() {
                return (itemStack, itemDisplayContext, stack, bufferSource, light, overlay) -> {
                    stack.translate(0, 0.5, 0);
                    stack.scale(3, 3, 3);
                    stack.mulPose(Axis.YP.rotationDegrees(180));
                    ModelsHolder.RINGS_CONTROLLER_ORI_BASE.bindTextureAndRender(stack);

                    for (var symbol : SymbolOriEnum.values()) {
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
