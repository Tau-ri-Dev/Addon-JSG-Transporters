package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.screen.element.tabs.Tab;
import dev.tauri.jsg.screen.element.tabs.TabAddress;
import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsg.stargate.network.StargateAddressDynamic;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Random;

public enum SymbolGoauldEnum implements SymbolInterface {
    ;

    @Override
    public boolean origin() {
        return false;
    }

    @Override
    public float getAngle() {
        return 0;
    }

    @Override
    public int getAngleIndex() {
        return 0;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getEnglishName() {
        return "";
    }

    @Override
    public ResourceLocation getIconResource(BiomeOverlayEnum overlay, ResourceKey<Level> dimensionId, int configOrigin) {
        return null;
    }

    @Override
    public String localize() {
        return "";
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return null;
    }

    @Override
    public boolean isValidForAddress() {
        return false;
    }

    @Override
    public SymbolInterface getNext(boolean previous) {
        return null;
    }

    // ------------------------------------------------------------
    // Static

    public static class Provider extends SymbolTypeEnum<SymbolGoauldEnum> {

        @Override
        public int[] getAncientTitlePos() {
            return new int[0];
        }

        @Override
        public Tab.TabBuilder finalizeAddressTab(Tab.TabBuilder builder) {
            return null;
        }

        @Override
        public TabAddress.SymbolCoords getSymbolCoords(int symbol) {
            return null;
        }

        @Override
        public SymbolGoauldEnum[] getValues() {
            return new SymbolGoauldEnum[0];
        }

        @Override
        public Block getBaseBlock() {
            return null;
        }

        @Override
        public Item getGlyphUpgrade() {
            return null;
        }

        @Override
        public Block getDHDBlock() {
            return null;
        }

        @Override
        public String getId() {
            return "";
        }

        @Override
        public SymbolGoauldEnum getBRB() {
            return null;
        }

        @Override
        public int getIconWidth() {
            return 0;
        }

        @Override
        public int getIconHeight() {
            return 0;
        }

        @Override
        public SymbolGoauldEnum getRandomSymbol(Random random) {
            return null;
        }

        @Override
        public SymbolGoauldEnum getOrigin() {
            return null;
        }

        @Override
        public int getMaxSymbolsDisplay(boolean hasUpgrade) {
            return 0;
        }

        @Override
        public int getMinimalSymbolCountTo(SymbolTypeEnum<?> symbolType, boolean localDial) {
            return 0;
        }

        @Override
        public boolean validateDialedAddress(StargateAddressDynamic stargateAddress) {
            return false;
        }

        @Override
        public float getAnglePerGlyph() {
            return 0;
        }

        @Override
        public SymbolGoauldEnum getSymbolByAngle(float angle) {
            return null;
        }

        @Override
        public SymbolGoauldEnum getTopSymbol() {
            return null;
        }

        @Override
        public SymbolGoauldEnum valueOf(int id) {
            return null;
        }

        @Override
        public SymbolGoauldEnum fromEnglishName(String englishName) {
            return null;
        }

        @Override
        public SymbolGoauldEnum getFirstValidForAddress() {
            return null;
        }
    }
}
