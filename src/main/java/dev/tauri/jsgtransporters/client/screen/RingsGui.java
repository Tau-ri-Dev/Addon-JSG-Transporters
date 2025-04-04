package dev.tauri.jsgtransporters.client.screen;

import dev.tauri.jsg.screen.element.tabs.TabbedContainerInterface;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class RingsGui extends AbstractContainerScreen<RingsContainer> implements TabbedContainerInterface {
    public RingsGui(RingsContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public List<Rect2i> getGuiExtraAreas() {
        return List.of();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
