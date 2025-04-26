package dev.tauri.jsgtransporters.client.screen.tab;

import dev.tauri.jsg.screen.base.JSGTextField;
import dev.tauri.jsg.screen.element.tabs.Tab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TabTRSettings extends Tab {

    private String name;
    private int dist;
    public JSGTextField nameTextField;
    public JSGTextField distanceTextField;
    int boxHeight = 10;
    int boxWidth = (107 - 50);

    private boolean keyTyped = false;

    public void setParams(String name, int dist) {
        this.name = name;
        this.dist = dist;
    }

    protected TabTRSettings(TabTRSettingsBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.dist = builder.dist;
        nameTextField = new JSGTextField(0, boxHeight,
                boxWidth, boxHeight, Component.empty());
        nameTextField.setValue(name);
        distanceTextField = new JSGTextField(0, boxHeight,
                boxWidth, boxHeight, Component.empty());
        distanceTextField.setValue(dist + "");
    }

    // todo(Mine): temporarily solution
    public void tryToUpdateInputs() {
        if (!keyTyped) {
            try {
                if (!(nameTextField.getValue().equals(name)))
                    nameTextField.setValue(name);
                if (Integer.parseInt(distanceTextField.getValue()) != dist)
                    distanceTextField.setValue(dist + "");
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!isVisible()) return;
        tryToUpdateInputs();
        super.render(graphics, mouseX, mouseY);

        int y = guiTop + defaultY + 20;
        int x = guiLeft + currentOffsetX + 15;
        nameTextField.setX(x);
        nameTextField.setY(y + boxHeight);
        nameTextField.render(graphics, mouseX, mouseY, 0);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.jsg_transporters.rings_name").getString(), x, y, 4210752, false);

        y += 22;
        distanceTextField.setX(x);
        distanceTextField.setY(y + boxHeight);
        distanceTextField.render(graphics, mouseX, mouseY, 0);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.jsg_transporters.rings_distance").getString(), x, y, 4210752, false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        nameTextField.mouseClicked(mouseX, mouseY, mouseButton);
        distanceTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if (nameTextField.keyPressed(typedChar, keyCode, 0)) {
            keyTyped = true;
            return true;
        }
        if (distanceTextField.keyPressed(typedChar, keyCode, 0)) {
            keyTyped = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (nameTextField.charTyped(typedChar, keyCode)) {
            keyTyped = true;
            return true;
        }
        if (distanceTextField.charTyped(typedChar, keyCode)) {
            keyTyped = true;
            return true;
        }

        return false;
    }

    @Override
    public void updateScreen() {
    }

    public static TabTRSettingsBuilder builder() {
        return new TabTRSettingsBuilder();
    }

    public static class TabTRSettingsBuilder extends TabBuilder {

        public String name;
        public int dist;

        public TabTRSettingsBuilder setParams(String name, int dist) {
            this.name = name;
            this.dist = dist;
            return this;
        }

        @Override
        public TabTRSettings build() {
            return new TabTRSettings(this);
        }
    }
}
