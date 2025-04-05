package dev.tauri.jsgtransporters.client.screen;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.forgeutil.SlotHandler;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.stargate.SaveConfigToServer;
import dev.tauri.jsg.screen.element.tabs.*;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.util.I18n;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.tauri.jsg.screen.inventory.stargate.StargateContainerGui.createConfigTab;
import static dev.tauri.jsg.screen.inventory.stargate.StargateContainerGui.createOverlayTab;

public class RingsGui extends AbstractContainerScreen<RingsContainer> implements TabbedContainerInterface {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(JSG.MOD_ID, "textures/gui/container_stargate.png");

    private final List<Tab> tabs = new ArrayList<>();
    private final Map<SymbolTypeEnum<?>, TabAddress> addressTabs = new LinkedHashMap<>();

    private final BlockPos pos;
    private TabConfig configTab;

    public RingsGui(RingsContainer container, Inventory pPlayerInventory, Component pTitle) {
        super(container, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 173;

        this.width = 176;
        this.height = 173;

        this.pos = container.ringsTile.getBlockPos();
    }

    @Override
    public void init() {
        super.init();

        tabs.clear();

        int i = 0;
        for (SymbolTypeEnum<?> type : SymbolTypeEnum.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            var tab = TabAddress.builder()
                    .setTextureLoader(type.getTextureLoader())
                    .setAddressProvider(menu.ringsTile)
                    .setSymbolType(type)
                    .setProgressColor(0x98BCF9)
                    .setGuiSize(imageWidth, imageHeight)
                    .setGuiPosition(leftPos, topPos)
                    .setTabPosition(-21, 11 + 22 * i)
                    .setOpenX(-128)
                    .setHiddenX(-6)
                    .setTabSize(128, 113)
                    .setTabTitle(I18n.format("gui.rings." + type.getId() + "_address"))
                    .setTabSide(TabSideEnum.LEFT);
            tab = type.finalizeAddressTab(tab);
            addressTabs.put(type, (TabAddress) tab.build());
            i++;
        }

        configTab = createConfigTab(menu.ringsTile.getConfig(), imageWidth, imageHeight, leftPos, topPos);

        TabBiomeOverlay overlayTab = createOverlayTab(menu.ringsTile.getSupportedOverlays(), imageWidth, imageHeight, leftPos, topPos);
        configTab.setOnTabClose(this::saveConfig);

        int ii = 0;
        for (var tab : addressTabs.values()) {
            if (ii + 7 == 10) ii++;
            menu.slots.set(ii + 7, tab.createSlot((SlotHandler) menu.getSlot(ii + 7)));
            ii++;
        }
        menu.slots.set(10, overlayTab.createAndSaveSlot((SlotHandler) menu.getSlot(10)));
    }

    @Override
    public List<Rect2i> getGuiExtraAreas() {
        return tabs.stream()
                .map(Tab::getArea)
                .collect(Collectors.toList());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    @Override
    public void onClose() {
        saveConfig();
        super.onClose();
    }

    private void saveConfig() {
        JSGPacketHandler.sendToServer(new SaveConfigToServer(pos, configTab.config));
        menu.ringsTile.setConfig(configTab.getConfig(true));
    }
}
