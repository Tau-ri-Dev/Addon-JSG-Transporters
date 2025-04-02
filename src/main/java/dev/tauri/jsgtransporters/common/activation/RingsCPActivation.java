package dev.tauri.jsgtransporters.common.activation;

import dev.tauri.jsg.renderer.activation.DHDActivation;
import dev.tauri.jsg.stargate.network.SymbolInterface;

public class RingsCPActivation extends DHDActivation {
    public RingsCPActivation(SymbolInterface textureKey, long stateChange, boolean dim) {
        super(textureKey, stateChange, dim);
    }

    @Override
    protected float getTickMultiplier() {
        return 1.0F;
    }
}
