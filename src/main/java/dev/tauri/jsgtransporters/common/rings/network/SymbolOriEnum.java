package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.screen.element.tabs.Tab;
import dev.tauri.jsg.screen.element.tabs.TabAddress;
import dev.tauri.jsg.stargate.BiomeOverlayEnum;
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
    ALPHA(0, 0, "Alpha"),
    BETA(1, 1, "Beta"),
    GAMMA(2, 2, "Gamma"),
    DELTA(3, 3, "Delta"),
    EPSILON(4, 4, "Epsilon"),
    ZETA(5, 5, "Zeta"),
    ETA(6, 6, "Eta"),
    THETA(7, 7, "Theta"),
    IOTA(8, 8, "Iota"),
    KAPPA(9, 9, "Kappa"),
    LAMBDA(10, 10, "Lambda"),
    MU(11, 11, "Mu"),
    NU(12, 12, "Nu"),
    XI(13, 13, "Xi"),
    OMICRON(14, 14, "Omicron"),
    PI(14, 14, "Pi"),
    RHO(15, 15, "Rho"),
    SIGMA(16, 16, "Sigma"),
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
        return this == ALPHA;
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
    public ResourceLocation getIconResource(BiomeOverlayEnum overlay, ResourceKey<Level> dimensionId, int configOrigin) {
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
            } while (valueOf(id) == null || !valueOf(id).isValidForAddress() || id == ALPHA.id);

            return valueOf(id);
        }

        @Override
        public SymbolOriEnum getOrigin() {
            return ALPHA;
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
        public SymbolOriEnum getSymbolByAngle(float angle) {
            return ALPHA;
        }

        @Override
        public SymbolOriEnum getTopSymbol() {
            return ALPHA;
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
            return BETA;
        }
    }
}
