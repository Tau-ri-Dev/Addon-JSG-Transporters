package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.screen.element.tabs.Tab;
import dev.tauri.jsg.screen.element.tabs.TabAddress;
import dev.tauri.jsg.stargate.BiomeOverlayRegistry;
import dev.tauri.jsg.stargate.network.IAddress;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.util.I18n;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.BlockRegistry;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum SymbolOriEnum implements SymbolInterface {
    PILLAR(0, 0, "Pillar"),
    CELESTIA(1, 1, "Celestia"),
    RADIANCE(2, 2, "Radiance"),
    JUDGMENT(3, 3, "Judgment"),
    CHALICE(4, 4, "Chalice"),
    DOMINION(5, 5, "Dominion"),
    HARMONY(6, 6, "Harmony"),
    PATH(7, 7, "Path"),
    OBLIVION(8, 8, "Oblivion"),
    SERENITY(9, 9, "Serenity"),
    ETERNUM(10, 10, "Eternum"),
    ORIGIN(11, 11, "Origin"),
    ASCENSION(12, 12, "Ascension"),
    SALVATION(13, 13, "Salvation"),
    FLAME(14, 14, "Flame"),
    CONVERGENCE(15, 15, "Convergence"),
    VIGIL(16, 16, "Vigil"),
    LIGHT(17, 17, "Light");

    public final int id;
    public final int angleIndex;

    public final String englishName;
    public final String translationKey;
    public final ResourceLocation iconResource;
    public final ResourceLocation modelResource;

    SymbolOriEnum(int id, int angleIndex, String englishName) {
        this.id = id;

        this.angleIndex = angleIndex;

        this.englishName = englishName;
        this.translationKey = "glyph.jsg_transporters.transportrings.ori." + englishName.toLowerCase().replace(" ", "_");
        this.iconResource = new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/symbol/rings/ori/" + englishName.toLowerCase() + ".png");
        this.modelResource = new ResourceLocation(JSGTransporters.MOD_ID, "models/tesr/rings/controller/ori/button_" + (id + 1) + ".obj");
    }

    @Override
    public boolean origin() {
        return this == LIGHT;
    }

    @Override
    public boolean brb() {
        return this == LIGHT;
    }

    @Override
    public float getAngle() {
        return angleIndex;
    }

    @Override
    public int getAngleIndex() {
        return angleIndex;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getEnglishName() {
        return englishName;
    }

    @Override
    public ResourceLocation getIconResource(BiomeOverlayRegistry.BiomeOverlayInstance overlay, ResourceKey<Level> dimensionId, int configOrigin) {
        return iconResource;
    }

    @Override
    public String localize() {
        return I18n.format(translationKey);
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return SymbolTypeRegistry.ORI;
    }

    @Override
    public boolean isValidForAddress() {
        return (this != LIGHT) && !origin();
    }

    @Override
    public SymbolInterface getNext(boolean previous) {
        var id = this.getId();
        while (true) {
            id += (previous ? -1 : 1);
            if (id < 0) id = 15;
            id = id % 16;
            var symbol = SymbolTypeRegistry.ORI.valueOf(id);
            if (symbol != null && symbol.isValidForAddress()) return symbol;
        }
    }

    // ------------------------------------------------------------
    // Static

    public static class Provider extends SymbolTypeEnum<SymbolOriEnum> {

        // used for rings gui - the title (u, v of the texture)
        @Override
        public int[] getAncientTitlePos() {
            return new int[]{330, 18};
        }

        @Override
        public Tab.TabBuilder finalizeAddressTab(Tab.TabBuilder builder) {
            return builder.setTexture(new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/container_transportrings.png"), 512)
                    .setBackgroundTextureLocation(176, 0)
                    .setIconRenderPos(0, 6)
                    .setIconSize(22, 22)
                    .setIconTextureLocation(304, 44);
        }

        @Override
        public TabAddress.SymbolCoords getSymbolCoords(int symbol) {
            // todo: this is copy from mw symbols... make it work when GUI is there
            return new TabAddress.SymbolCoords(29 + 31 * (symbol % 3), 20 + 28 * (symbol / 3));
        }

        @Override
        public SymbolOriEnum[] getValues() {
            return SymbolOriEnum.values();
        }

        @Override
        public Block getBaseBlock() {
            return BlockRegistry.RINGS_ORI.get();
        }

        @Override
        public Item getGlyphUpgrade() {
            return ItemRegistry.CRYSTAL_GLYPH_ORI.get();
        }

        @Override
        public Block getDHDBlock() {
            return null;
        }

        @Override
        public String getId() {
            return "ori";
        }

        @Override
        public SymbolOriEnum getBRB() {
            return LIGHT;
        }

        @Override
        public int getIconWidth() {
            return 32;
        }

        @Override
        public int getIconHeight() {
            return 32;
        }

        @Override
        public SymbolOriEnum getRandomSymbol(Random random) {
            int id;
            do {
                id = random.nextInt(16);
            } while (valueOf(id) == null || !valueOf(id).isValidForAddress() || id == LIGHT.id);

            return valueOf(id);
        }

        @Override
        public SymbolOriEnum getOrigin() {
            return LIGHT;
        }

        @Override
        public int getMaxSymbolsDisplay(boolean hasUpgrade) {
            return 4;
        }

        @Override
        public int getMinimalSymbolCountTo(SymbolTypeEnum<?> symbolType, boolean localDial) {
            return 4;
        }

        @Override
        public boolean validateDialedAddress(IAddress address) {
            if (address.getSize() < 5)
                return false;

            return address.get(address.getSize() - 1).origin();
        }

        @Override
        public float getAnglePerGlyph() {
            return 0;
        }

        @Override
        public SymbolOriEnum getSymbolByAngle(float v, float v1) {
            return null;
        }

        @Override
        public SymbolOriEnum getSymbolByAngle(float angle) {
            return PILLAR;
        }

        @Override
        public SymbolOriEnum getTopSymbol() {
            return PILLAR;
        }

        private static final Map<Integer, SymbolOriEnum> ID_MAP = new HashMap<>();
        private static final Map<String, SymbolOriEnum> ENGLISH_NAME_MAP = new HashMap<>();

        static {
            for (SymbolOriEnum symbol : SymbolOriEnum.values()) {
                ID_MAP.put(symbol.id, symbol);
                ENGLISH_NAME_MAP.put(symbol.englishName.toLowerCase(), symbol);
            }
        }

        @Override
        public SymbolOriEnum valueOf(int id) {
            return ID_MAP.get(id);
        }

        @Override
        public SymbolOriEnum fromEnglishName(String englishName) {
            return ENGLISH_NAME_MAP.get(englishName.toLowerCase());
        }

        @Override
        public SymbolOriEnum getFirstValidForAddress() {
            return CELESTIA;
        }
    }
}
