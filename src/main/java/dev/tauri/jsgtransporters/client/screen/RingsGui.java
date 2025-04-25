package dev.tauri.jsgtransporters.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.tauri.jsg.config.JSGConfig;
import dev.tauri.jsg.forgeutil.SlotHandler;
import dev.tauri.jsg.loader.texture.Texture;
import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.packets.SetOpenTabToServer;
import dev.tauri.jsg.packet.packets.stargate.SaveConfigToServer;
import dev.tauri.jsg.power.general.LargeEnergyStorage;
import dev.tauri.jsg.screen.element.tabs.*;
import dev.tauri.jsg.screen.util.GuiHelper;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.util.I18n;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static dev.tauri.jsg.screen.inventory.stargate.StargateContainerGui.createConfigTab;
import static dev.tauri.jsg.screen.inventory.stargate.StargateContainerGui.createOverlayTab;
import static dev.tauri.jsg.screen.util.GuiHelper.*;

public class RingsGui extends AbstractContainerScreen<RingsContainer> implements TabbedContainerInterface {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/container_transportrings.png");

    private final List<Tab> tabs = new ArrayList<>();
    private final Map<SymbolTypeEnum<?>, TabAddress> addressTabs = new LinkedHashMap<>();

    private final BlockPos pos;
    private TabConfig configTab;

    private int energyStored;
    private int maxEnergyStored;

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
                    .setTabTitle(I18n.format("gui.stargate." + type.getId() + "_address"))
                    .setTabSide(TabSideEnum.LEFT);
            tab = type.finalizeAddressTab(tab);
            addressTabs.put(type, (TabAddress) tab.build());
            i++;
        }

        configTab = createConfigTab(menu.ringsTile.getConfig(), imageWidth, imageHeight, leftPos, topPos);

        TabBiomeOverlay overlayTab = createOverlayTab(menu.ringsTile.getSupportedOverlays(), imageWidth, imageHeight, leftPos, topPos);
        configTab.setOnTabClose(this::saveConfig);

        tabs.addAll(addressTabs.values());
        tabs.add(configTab);

        tabs.add(overlayTab);

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
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
        GuiHelper.currentStack = graphics.pose();

        if (menu.ringsTile.getConfig().getOptions().size() != configTab.getConfig(false).getOptions().size())
            configTab.updateConfig(menu.ringsTile.getConfig(), true);
        graphics.pose().pushPose();
        for (Tab tab : tabs) {
            tab.render(graphics, mouseX, mouseY);
        }
        graphics.pose().popPose();
        graphics.pose().translate(0, 0, 0.2f);

        Texture.bindTextureWithMc(BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        drawModalRectWithCustomSizedTexture(leftPos, topPos, 0, 0, imageWidth, imageHeight, 512, 512);

        // Draw cross on inactive capacitors
        for (int i = 0; i < 3 - menu.ringsTile.getSupportedCapacitors(); i++) {
            drawModalRectWithCustomSizedTexture(leftPos + 151 - 18 * i, topPos + 27, 24, 180, 16, 16, 512, 512);
        }

        for (int i = menu.ringsTile.getPowerTier(); i < 4; i++)
            drawModalRectWithCustomSizedTexture(leftPos + 10 + 39 * i, topPos + 69, 0, 173, 39, 6, 512, 512);

        int width = Math.round((energyStored / (float) JSGConfig.Stargate.stargateEnergyStorage.get() * 156));
        drawGradientRect(graphics.pose(), leftPos + 10, topPos + 69, leftPos + 10 + width, topPos + 69 + 6, 0xffcc2828, 0xff731616);

        // Draw ancient title
        int[] pos = menu.ringsTile.getSymbolType().getAncientTitlePos();
        drawModalRectWithCustomSizedTexture(leftPos + 137, topPos + 4, pos[0], pos[1], 35, 8, 512, 512);

        boolean drawICFirstCable = false;

        // Draw cables
        for (int i = 0; i < 7; i++) {
            if (menu.getSlot(i).hasItem()) {
                if (i < 4) drawICFirstCable = true;
                // render activated wires/cables
                switch (i) {
                    // upgrades
                    case 0:
                        drawModalRectWithCustomSizedTexture(leftPos + 16, topPos + 44, 11, 239, 32 - 10, 254 - 238, 512, 512);
                        break;
                    case 1:
                        drawModalRectWithCustomSizedTexture(leftPos + 34, topPos + 44, 7, 237, 4, 10, 512, 512);
                        break;
                    case 2:
                        drawModalRectWithCustomSizedTexture(leftPos + 50, topPos + 44, 2, 237, 4, 10, 512, 512);
                        break;
                    case 3:
                        drawModalRectWithCustomSizedTexture(leftPos + 50, topPos + 44, 0, 255, 22, 270 - 254, 512, 512);
                        break;

                    // capacitors
                    case 4:
                        drawModalRectWithCustomSizedTexture(leftPos + 121, topPos + 44, 0, 225, 14, 236 - 224, 512, 512);
                        break;
                    case 5:
                        drawModalRectWithCustomSizedTexture(leftPos + 139, topPos + 44, 14, 225, 4, 230 - 224, 512, 512);
                        break;
                    case 6:
                        drawModalRectWithCustomSizedTexture(leftPos + 147, topPos + 44, 18, 225, 31 - 17, 238 - 224, 512, 512);
                        break;
                    default:
                        break;
                }
            }
        }

        // render cables from 1. IC to power line
        if (drawICFirstCable) {
            drawModalRectWithCustomSizedTexture(leftPos + 41, topPos + 62, 0, 239, 2, 6, 512, 512);
            drawModalRectWithCustomSizedTexture(leftPos + 45, topPos + 62, 11, 239, 2, 6, 512, 512);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableDepthTest();

        renderTransparentBackground(graphics, this);

        boolean hasAddressUpgrade = false;

        for (var entry : addressTabs.entrySet()) {
            entry.getValue().setVisible(false);
        }

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = menu.getSlot(i).getItem();

            if (!itemStack.isEmpty()) {
                for (var entry : addressTabs.entrySet()) {
                    if (itemStack.getItem() == entry.getKey().getGlyphUpgrade()) {
                        entry.getValue().setVisible(true);
                    }
                }
            }
        }

        for (var entry : addressTabs.entrySet()) {
            entry.getValue().setMaxSymbols(entry.getKey().getMaxSymbolsDisplay(hasAddressUpgrade));
        }
        configTab.setVisible(menu.hasCreative);

        Tab.updatePositions(tabs);

        LargeEnergyStorage energyStorageInternal = (LargeEnergyStorage) menu.ringsTile.getCapability(ForgeCapabilities.ENERGY, null).resolve().orElseThrow();
        energyStored = energyStorageInternal.getEnergyStoredInternally();
        maxEnergyStored = energyStorageInternal.getMaxEnergyStoredInternally();

        for (int i = 4; i < 7; i++) {
            Optional<IEnergyStorage> energyStorage = menu.getSlot(i).getItem().getCapability(ForgeCapabilities.ENERGY, null).resolve();

            if (energyStorage.isEmpty())
                continue;

            energyStored += energyStorage.get().getEnergyStored();
            maxEnergyStored += energyStorage.get().getMaxEnergyStored();
        }

        for (int i = 7; i < 11; i++) {
            Tab.SlotTab slot = ((Tab.SlotTab) menu.getSlot(i)).updatePos();
            slot.setSlotIndex(i);
            menu.slots.set(i, slot);
        }

        graphics.pose().pushPose();
        super.render(graphics, mouseX, mouseY, partialTicks);

        renderTooltip(graphics, mouseX, mouseY);
        graphics.pose().popPose();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
        String caps = I18n.format("gui.stargate.capacitors");
        graphics.drawString(font, caps, this.imageWidth - 8 - font.width(caps), 16, 4210752, false);

        String energyPercent = String.format("%.2f", energyStored / (float) maxEnergyStored * 100) + " %";
        graphics.drawString(font, energyPercent, this.imageWidth - 8 - font.width(energyPercent), 79, 4210752, false);

        graphics.drawString(font, I18n.format("gui.upgrades"), 8, 16, 4210752, false);
        graphics.drawString(font, I18n.format("container.inventory"), 8, imageHeight - 96 + 2, 4210752, false);

        for (Tab tab : tabs) {
            tab.renderFg(graphics, mouseX, mouseY);
        }

        int transferred = menu.ringsTile.getEnergyTransferredLastTick();
        ChatFormatting transferredFormatting = ChatFormatting.GRAY;
        String transferredSign = "";

        if (transferred > 0) {
            transferredFormatting = ChatFormatting.GREEN;
            transferredSign = "+";
        } else if (transferred < 0) {
            transferredFormatting = ChatFormatting.RED;
        }

        if (isPointInRegion(10, 69, 156, 6, mouseX - getGuiLeft(), mouseY - getGuiTop())) {
            List<String> power = Arrays.asList(
                    I18n.format("gui.stargate.energyBuffer"),
                    ChatFormatting.GRAY + String.format("%,d / %,d RF", energyStored, maxEnergyStored),
                    transferredFormatting + transferredSign + String.format("%,d RF/t", transferred));
            drawHoveringText(graphics, font, power, mouseX - leftPos, mouseY - topPos);
        }
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);

            if (tab.isCursorOnTab((int) mouseX, (int) mouseY)) {
                if (Tab.tabsInteract(tabs, i)) {
                    menu.setOpenTabId(i);
                } else {
                    menu.setOpenTabId(-1);
                }

                JSGPacketHandler.sendToServer(new SetOpenTabToServer(menu.getOpenTabId()));

                break;
            }

        }
        for (Tab tab : tabs) {
            if (tab.isOpen() && tab.isVisible()) {
                tab.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            }
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double v, double v1, double v2) {
        super.mouseScrolled(v, v1, v2);
        int wheel = (int) v2;
        if (wheel != 0) {
            for (Tab tab : tabs) {
                if (tab instanceof TabScrollAble && tab.isVisible() && tab.isOpen()) {
                    if (tab.isCursorOnTabBody((int) v, (int) v1)) {
                        ((TabScrollAble) tab).scroll(wheel);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int typedChar, int keyCode, int t) {
        for (Tab tab : tabs) {
            if (tab.isOpen() && tab.isVisible()) {
                if (tab.keyTyped((char) typedChar, keyCode))
                    return true;
            }
        }
        return super.keyPressed(typedChar, keyCode, t);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        for (Tab tab : tabs) {
            if (tab.isOpen() && tab.isVisible()) {
                if (tab.charTyped(typedChar, keyCode))
                    return true;
            }
        }
        return super.charTyped(typedChar, keyCode);
    }
}
