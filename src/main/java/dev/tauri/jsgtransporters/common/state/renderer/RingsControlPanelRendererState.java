package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.renderer.activation.Activation;
import dev.tauri.jsg.renderer.activation.DHDActivation;
import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.state.State;
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

    protected BiomeOverlayEnum biomeOverlay;
    public BiomeOverlayEnum biomeOverride;

    public BiomeOverlayEnum getBiomeOverlay() {
        if (biomeOverride != null)
            return biomeOverride;

        return biomeOverlay;
    }

    public abstract ResourceLocation getButtonTexture(SymbolInterface symbol, BiomeOverlayEnum biomeOverlay);


    public void activateSymbol(long totalWorldTime, SymbolInterface symbol) {
        activationList.add(new DHDActivation(symbol, totalWorldTime, false));
    }

    public void setBiomeOverlay(BiomeOverlayEnum biomeOverlay) {
        this.biomeOverlay = biomeOverlay;
    }

    public void iterate(Level world, double partialTicks) {
        Activation.iterate(activationList, world.getGameTime(), partialTicks, (symbol, stage) -> {
            if (stage >= 5 && BUTTON_STATE_MAP.get(symbol) != Math.round(stage)) {
                activationList.add(new DHDActivation(symbol, world.getGameTime(), true));
            }
            BUTTON_STATE_MAP.put(symbol, Math.round(stage));
        });
    }

    public boolean isButtonActive(SymbolInterface symbol) {
        var val = BUTTON_STATE_MAP.get(symbol);
        if (val == null) return false;
        return val >= 5;
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }
}
