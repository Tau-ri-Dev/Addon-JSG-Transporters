package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsgcore.common.entity.BiomeOverlayInstance;
import dev.tauri.jsgcore.common.registry.CoreBiomeOverlays;
import dev.tauri.jsgcore.common.symbol.SymbolInterface;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RingsAncientCPRendererState extends RingsControlPanelRendererState {
    private static final String SYMBOL_TEXTURE_BASE = "textures/tesr/rings/controller/ancient/button_";
    private static final String SYMBOL_TEXTURE_END = "png";
    private static final Map<BiomeOverlayInstance, Map<Integer, ResourceLocation>> BIOME_TEXTURE_MAP = new HashMap<>();

    static {
        for (BiomeOverlayInstance biomeOverlay : BiomeOverlayInstance.values()) {
            var map = new HashMap<Integer, ResourceLocation>();
            for (int i = 0; i <= 5; i++) {
                map.put(i, JSGMapping.rl(JSGTransporters.MOD_ID, SYMBOL_TEXTURE_BASE + i + biomeOverlay.suffix() + "." + SYMBOL_TEXTURE_END));
            }

            BIOME_TEXTURE_MAP.put(biomeOverlay, map);

        }
    }

    @Override
    public ResourceLocation getButtonTexture(SymbolInterface symbol, BiomeOverlayInstance biomeOverlay) {
        var val = getButtonState(symbol);
        if (biomeOverlay == null) biomeOverlay = CoreBiomeOverlays.NORMAL.get();
        return BIOME_TEXTURE_MAP.get(biomeOverlay).get(val);
    }
}
