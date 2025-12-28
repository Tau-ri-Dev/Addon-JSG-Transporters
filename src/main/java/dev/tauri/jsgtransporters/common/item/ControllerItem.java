package dev.tauri.jsgtransporters.common.item;

import dev.tauri.jsg.api.item.JSGBlockItem;
import dev.tauri.jsg.helpers.ItemHelper;
import dev.tauri.jsg.item.JSGModelOBJInGUIRenderer;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

public class ControllerItem extends JSGBlockItem {
    public ControllerItem(Block pBlock) {
        super(pBlock, new Item.Properties(), List.of(TabRegistry.TAB_RINGS));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private static final JSGModelOBJInGUIRenderer instance = new JSGModelOBJInGUIRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                instance.renderPartInterface = getRenderPartInterface();
                return instance;
            }
        });
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        ItemHelper.applyGenericToolTip(this.getDescriptionId(), components, tooltipFlag);
    }

    @OnlyIn(Dist.CLIENT)
    public JSGModelOBJInGUIRenderer.RenderPartInterface getRenderPartInterface(){
        return null;
    }
}
