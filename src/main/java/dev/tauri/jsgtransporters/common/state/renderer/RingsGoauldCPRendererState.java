package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RingsGoauldCPRendererState extends RingsControlPanelRendererState {
    private static final String SYMBOL_TEXTURE_BASE = "textures/tesr/rings/controller/goauld/goauld_button_";
    private static final String LIGHT_TEXTURE_BASE = "textures/tesr/rings/controller/goauld/goauld_light_";
    private static final String SYMBOL_TEXTURE_END = "jpg";
    private static final String LIGHT_TEXTURE_END = "jpg";
    private static final Map<BiomeOverlayEnum, Map<Integer, ResourceLocation>> BIOME_TEXTURE_MAP = new HashMap<>();
    private static final Map<BiomeOverlayEnum, Map<Integer, ResourceLocation>> BIOME_TEXTURE_MAP_LIGHT = new HashMap<>();

    static {
        for (BiomeOverlayEnum biomeOverlay : BiomeOverlayEnum.values()) {
            var map = new HashMap<Integer, ResourceLocation>();
            var lightMap = new HashMap<Integer, ResourceLocation>();
            for (int i = 0; i <= 5; i++) {
                map.put(i, new ResourceLocation(JSGTransporters.MOD_ID, SYMBOL_TEXTURE_BASE + i + biomeOverlay.getSuffix() + "." + SYMBOL_TEXTURE_END));
                lightMap.put(i, new ResourceLocation(JSGTransporters.MOD_ID, LIGHT_TEXTURE_BASE + i + biomeOverlay.getSuffix() + "." + LIGHT_TEXTURE_END));
            }

            BIOME_TEXTURE_MAP.put(biomeOverlay, map);
            BIOME_TEXTURE_MAP_LIGHT.put(biomeOverlay, lightMap);
        }
    }

    @Override
    public ResourceLocation getButtonTexture(SymbolInterface symbol, BiomeOverlayEnum biomeOverlay) {
        var val = getButtonState(symbol);
        if (biomeOverlay == null) biomeOverlay = BiomeOverlayEnum.NORMAL;
        if (symbol.brb())
            return BIOME_TEXTURE_MAP_LIGHT.get(biomeOverlay).get(val);

        return BIOME_TEXTURE_MAP.get(biomeOverlay).get(val);
    }
}
