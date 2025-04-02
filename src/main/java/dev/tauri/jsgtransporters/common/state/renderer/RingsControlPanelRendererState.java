package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.renderer.activation.Activation;
import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.state.State;
import dev.tauri.jsgtransporters.common.activation.RingsCPActivation;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RingsControlPanelRendererState extends State {
    public RingsControlPanelRendererState() {
    }

    public List<Activation<SymbolInterface>> activationList = new ArrayList<>();
    public final Map<SymbolInterface, Integer> BUTTON_STATE_MAP = new HashMap<>();
    public final Map<SymbolInterface, Float> ACTUAL_BUTTON_STATE_MAP = new HashMap<>();

    protected BiomeOverlayEnum biomeOverlay;
    public BiomeOverlayEnum biomeOverride;
    private boolean clearingSymbols;

    public BiomeOverlayEnum getBiomeOverlay() {
        if (biomeOverride != null)
            return biomeOverride;

        return biomeOverlay;
    }

    public abstract ResourceLocation getButtonTexture(SymbolInterface symbol, BiomeOverlayEnum biomeOverlay);


    public void activateSymbol(long totalWorldTime, SymbolInterface symbol) {
        activationList.add(new RingsCPActivation(symbol, totalWorldTime, false));
        clearingSymbols = false;
    }

    public void clearSymbols(long totalWorldTime) {
        activationList.clear();
        for (var e : BUTTON_STATE_MAP.entrySet()) {
            if (e.getValue() < 1) continue;
            activationList.add(new RingsCPActivation(e.getKey(), totalWorldTime, true));
        }
        BUTTON_STATE_MAP.clear();
        ACTUAL_BUTTON_STATE_MAP.clear();
        clearingSymbols = true;
    }


    public void setBiomeOverlay(BiomeOverlayEnum biomeOverlay) {
        this.biomeOverlay = biomeOverlay;
    }

    public void iterate(Level world, double partialTicks) {
        Activation.iterate(activationList, world.getGameTime(), partialTicks, (symbol, stage) -> {
            BUTTON_STATE_MAP.put(symbol, Math.round(stage));
            if (ACTUAL_BUTTON_STATE_MAP.getOrDefault(symbol, 0f) < stage || clearingSymbols)
                ACTUAL_BUTTON_STATE_MAP.put(symbol, stage);
        });
    }

    public boolean isButtonActive(SymbolInterface symbol) {
        var val = BUTTON_STATE_MAP.get(symbol);
        if (val == null) return false;
        return val >= 5;
    }

    public int getButtonState(SymbolInterface symbol) {
        Integer val = BUTTON_STATE_MAP.get(symbol);
        if (val == null) val = 0;
        return val;
    }

    public float getActualButtonState(SymbolInterface symbol) {
        Float val = ACTUAL_BUTTON_STATE_MAP.get(symbol);
        if (val == null) val = 0f;
        return val;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }
}
