package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.api.client.model.IModelLoader;
import dev.tauri.jsg.api.client.screen.ITab;
import dev.tauri.jsg.api.client.texture.ITextureLoader;
import dev.tauri.jsg.api.pointoforigins.IPointOfOriginType;
import dev.tauri.jsg.api.pointoforigins.PointOfOrigin;
import dev.tauri.jsg.api.stargate.network.address.IAddress;
import dev.tauri.jsg.api.stargate.network.address.symbol.SymbolInterface;
import dev.tauri.jsg.api.stargate.network.address.symbol.types.AbstractSymbolType;
import dev.tauri.jsg.api.util.I18n;
import dev.tauri.jsg.screen.element.tabs.TabAddress;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.BlockRegistry;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum SymbolGoauldEnum implements SymbolInterface {
    AMUN(0, 0, "Amun"),
    SERKET(1, 1, "Serket"),
    KHEPRI(2, 3, "Khepri"),
    RA(3, 2, "Ra"),
    FELLUCA(4, 4, "Felluca"),
    COBRA(5, 5, "Cobra"),
    LIGHT(6, 6, "Light");

    public final int id;
    public final int angleIndex;

    public final String englishName;
    public final String translationKey;
    public final ResourceLocation iconResource;
    public final ResourceLocation modelResource;

    SymbolGoauldEnum(int id, int angleIndex, String englishName) {
        this.id = id;

        this.angleIndex = angleIndex;

        this.englishName = englishName;
        this.translationKey = "glyph.jsg_transporters.transportrings.goauld." + englishName.toLowerCase().replace(" ", "_");
        this.iconResource = new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/symbol/rings/goauld/" + englishName.toLowerCase() + ".png");
        if (id == 6)
            this.modelResource = new ResourceLocation(JSGTransporters.MOD_ID, "models/tesr/rings/controller/goauld/indicator_lights.obj");
        else
            this.modelResource = new ResourceLocation(JSGTransporters.MOD_ID, "models/tesr/rings/controller/goauld/goauld_button_" + (id + 1) + ".obj");
    }

    @Override
    public boolean origin() {
        return this == COBRA;
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
    public ResourceLocation getIconResource(@Nullable PointOfOrigin pointOfOrigin) {
        return iconResource;
    }

    @Override
    public ResourceLocation getModelResource(IPointOfOriginType iPointOfOriginType, @Nullable PointOfOrigin pointOfOrigin, String s) {
        return modelResource;
    }

    @Override
    public String localize() {
        return I18n.format(translationKey);
    }

    @Override
    public AbstractSymbolType<?> getSymbolType() {
        return SymbolTypeRegistry.GOAULD;
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
            if (id < 0) id = 5;
            id = id % 6;
            var symbol = SymbolTypeRegistry.GOAULD.valueOf(id);
            if (symbol != null && symbol.isValidForAddress()) return symbol;
        }
    }

    @Override
    public boolean renderIconByMinecraft() {
        return true;
    }

    // ------------------------------------------------------------
    // Static

    public static class Provider extends AbstractSymbolType<SymbolGoauldEnum> {
        @Override
        public ITextureLoader getTextureLoader() {
            return Constants.LOADERS_HOLDER.texture();
        }

        @Override
        public IModelLoader getModelLoader() {
            return Constants.LOADERS_HOLDER.model();
        }

        // used for rings gui - the title (u, v of the texture)
        @Override
        public int[] getAncientTitlePos() {
            return new int[]{330, 0};
        }

        @Override
        public ITab.ITabBuilder finalizeAddressTab(ITab.ITabBuilder builder) {
            return builder.setTexture(new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/container_transportrings.png"), 512)
                    .setBackgroundTextureLocation(176, 0)
                    .setIconRenderPos(0, 6)
                    .setIconSize(22, 22)
                    .setIconTextureLocation(304, 22);
        }

        @Override
        public TabAddress.SymbolCoords getSymbolCoords(int symbol) {
            // todo: this is copy from mw symbols... make it work when GUI is there
            return new TabAddress.SymbolCoords(29 + 31 * (symbol % 3), 20 + 28 * (symbol / 3));
        }

        @Override
        public SymbolGoauldEnum[] getValues() {
            return SymbolGoauldEnum.values();
        }

        @Override
        public Block getBaseBlock() {
            return BlockRegistry.RINGS_GOAULD.get();
        }

        @Override
        public Item getGlyphUpgrade() {
            return ItemRegistry.CRYSTAL_GLYPH_GOAULD.get();
        }

        @Override
        public Block getDHDBlock() {
            return null;
        }

        @Override
        public String getId() {
            return "goauld";
        }

        @Override
        public SymbolGoauldEnum getBRB() {
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
        public SymbolGoauldEnum getRandomSymbol(Random random) {
            int id;
            do {
                id = random.nextInt(16);
            } while (valueOf(id) == null || !valueOf(id).isValidForAddress() || id == COBRA.id);

            return valueOf(id);
        }

        @Override
        public SymbolGoauldEnum getOrigin() {
            return COBRA;
        }

        @Override
        public int getMaxSymbolsDisplay(boolean hasUpgrade) {
            return 4;
        }

        @Override
        public int getMinimalSymbolCountTo(AbstractSymbolType<?> symbolType, boolean localDial) {
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
        public SymbolGoauldEnum getSymbolByAngle(float v, float v1) {
            return null;
        }

        @Override
        public SymbolGoauldEnum getSymbolByAngle(float angle) {
            return COBRA;
        }

        @Override
        public SymbolGoauldEnum getTopSymbol() {
            return COBRA;
        }

        private static final Map<Integer, SymbolGoauldEnum> ID_MAP = new HashMap<>();
        private static final Map<String, SymbolGoauldEnum> ENGLISH_NAME_MAP = new HashMap<>();

        static {
            for (SymbolGoauldEnum symbol : SymbolGoauldEnum.values()) {
                ID_MAP.put(symbol.id, symbol);
                ENGLISH_NAME_MAP.put(symbol.englishName.toLowerCase(), symbol);
            }
        }

        @Override
        public SymbolGoauldEnum valueOf(int id) {
            return ID_MAP.get(id);
        }

        @Override
        public SymbolGoauldEnum fromEnglishName(String englishName) {
            return ENGLISH_NAME_MAP.get(englishName.toLowerCase());
        }

        @Override
        public SymbolGoauldEnum getFirstValidForAddress() {
            return AMUN;
        }

        @Override
        public IPointOfOriginType getPointOfOriginType() {
            return null;
        }
    }
}
