package dev.tauri.jsgtransporters.common.activation;

import dev.tauri.jsg.api.stargate.network.address.symbol.SymbolInterface;
import dev.tauri.jsg.renderer.activation.DHDActivation;

public class RingsCPActivation extends DHDActivation {
    public RingsCPActivation(SymbolInterface textureKey, long stateChange, boolean dim) {
        super(textureKey, stateChange, dim);
    }

    @Override
    protected float getTickMultiplier() {
        return 1.0F;
    }
}
