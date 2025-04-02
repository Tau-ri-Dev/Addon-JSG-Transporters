package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RingsOriCPRendererState extends RingsControlPanelRendererState {
    private static final String SYMBOL_TEXTURE_BASE = "textures/tesr/rings/controller/ori/button_";
    private static final String SYMBOL_TEXTURE_END = "png";
    private static final Map<BiomeOverlayEnum, Map<Integer, ResourceLocation>> BIOME_TEXTURE_MAP = new HashMap<>();

    static {
        for (BiomeOverlayEnum biomeOverlay : BiomeOverlayEnum.values()) {
            var map = new HashMap<Integer, ResourceLocation>();
            for (int i = 0; i <= 5; i++) {
                map.put(i, new ResourceLocation(JSGTransporters.MOD_ID, SYMBOL_TEXTURE_BASE + i + biomeOverlay.getSuffix() + "." + SYMBOL_TEXTURE_END));
            }

            BIOME_TEXTURE_MAP.put(biomeOverlay, map);

        }
    }

    @Override
    public ResourceLocation getButtonTexture(SymbolInterface symbol, BiomeOverlayEnum biomeOverlay) {
        var val = getButtonState(symbol);
        if (biomeOverlay == null) biomeOverlay = BiomeOverlayEnum.NORMAL;
        return BIOME_TEXTURE_MAP.get(biomeOverlay).get(val);
    }
}
