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
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.ClientConstants;
import dev.tauri.jsgtransporters.common.registry.BlockRegistry;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum SymbolAncientEnum implements SymbolInterface {
    SELEN(0, 0, "Selen"),
    TIRIS(1, 1, "Tiris"),
    VARUN(2, 2, "Varun"),
    ELYRA(3, 3, "Elyra"),
    ANKOR(4, 4, "Ankor"),
    SYTHIS(5, 5, "Sythis"),
    AURIN(6, 6, "Aurin"),
    VELAR(7, 7, "Velar"),
    OMNIS(8, 8, "Omnis"),
    LIGHT(9, 9, "Light");

    public final int id;
    public final int angleIndex;

    public final String englishName;
    public final String translationKey;
    public final ResourceLocation iconResource;
    public final ResourceLocation modelResource;

    SymbolAncientEnum(int id, int angleIndex, String englishName) {
        this.id = id;

        this.angleIndex = angleIndex;

        this.englishName = englishName;
        this.translationKey = "glyph.jsg_transporters.transportrings.ancient." + englishName.toLowerCase().replace(" ", "_");
        this.iconResource = new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/symbol/rings/ancient/" + englishName.toLowerCase() + ".png");
        this.modelResource = new ResourceLocation(JSGTransporters.MOD_ID, "models/tesr/rings/controller/ancient/button_" + (id + 1) + ".obj");
    }

    @Override
    public boolean origin() {
        return this == OMNIS;
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
        return SymbolTypeRegistry.ANCIENT;
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
            if (id < 0) id = 8;
            id = id % 9;
            var symbol = SymbolTypeRegistry.ANCIENT.valueOf(id);
            if (symbol != null && symbol.isValidForAddress()) return symbol;
        }
    }

    @Override
    public boolean renderIconByMinecraft() {
        return true;
    }

    // ------------------------------------------------------------
    // Static

    public static class Provider extends AbstractSymbolType<SymbolAncientEnum> {
        @Override
        public ITextureLoader getTextureLoader() {
            return ClientConstants.LOADERS_HOLDER.texture();
        }

        @Override
        public IModelLoader getModelLoader() {
            return ClientConstants.LOADERS_HOLDER.model();
        }

        // used for rings gui - the title (u, v of the texture)
        @Override
        public int[] getAncientTitlePos() {
            return new int[]{330, 36};
        }

        @Override
        public ITab.ITabBuilder finalizeAddressTab(ITab.ITabBuilder builder) {
            return builder.setTexture(new ResourceLocation(JSGTransporters.MOD_ID, "textures/gui/container_transportrings.png"), 512)
                    .setBackgroundTextureLocation(176, 0)
                    .setIconRenderPos(0, 6)
                    .setIconSize(22, 22)
                    .setIconTextureLocation(304, 66);
        }

        @Override
        public TabAddress.SymbolCoords getSymbolCoords(int symbol) {
            // todo: this is copy from mw symbols... make it work when GUI is there
            return new TabAddress.SymbolCoords(29 + 31 * (symbol % 3), 20 + 28 * (symbol / 3));
        }

        @Override
        public SymbolAncientEnum[] getValues() {
            return SymbolAncientEnum.values();
        }

        @Override
        public Block getBaseBlock() {
            return BlockRegistry.RINGS_ANCIENT.get();
        }

        @Override
        public Item getGlyphUpgrade() {
            return ItemRegistry.CRYSTAL_GLYPH_ANCIENT.get();
        }

        @Override
        public Block getDHDBlock() {
            return null;
        }

        @Override
        public String getId() {
            return "ancient";
        }

        @Override
        public SymbolAncientEnum getBRB() {
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
        public SymbolAncientEnum getRandomSymbol(Random random) {
            int id;
            do {
                id = random.nextInt(16);
            } while (valueOf(id) == null || !valueOf(id).isValidForAddress() || id == OMNIS.id);

            return valueOf(id);
        }

        @Override
        public SymbolAncientEnum getOrigin() {
            return OMNIS;
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
        public SymbolAncientEnum getSymbolByAngle(float v, float v1) {
            return null;
        }

        @Override
        public SymbolAncientEnum getSymbolByAngle(float angle) {
            return OMNIS;
        }

        @Override
        public SymbolAncientEnum getTopSymbol() {
            return OMNIS;
        }

        private static final Map<Integer, SymbolAncientEnum> ID_MAP = new HashMap<>();
        private static final Map<String, SymbolAncientEnum> ENGLISH_NAME_MAP = new HashMap<>();

        static {
            for (SymbolAncientEnum symbol : SymbolAncientEnum.values()) {
                ID_MAP.put(symbol.id, symbol);
                ENGLISH_NAME_MAP.put(symbol.englishName.toLowerCase(), symbol);
            }
        }

        @Override
        public SymbolAncientEnum valueOf(int id) {
            return ID_MAP.get(id);
        }

        @Override
        public SymbolAncientEnum fromEnglishName(String englishName) {
            return ENGLISH_NAME_MAP.get(englishName.toLowerCase());
        }

        @Override
        public SymbolAncientEnum getFirstValidForAddress() {
            return SELEN;
        }

        @Override
        public IPointOfOriginType getPointOfOriginType() {
            return null;
        }
    }
}
