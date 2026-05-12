package dev.tauri.jsgtransporters.common.activation;


import dev.tauri.jsg.core.client.renderer.Activation;
import dev.tauri.jsg.core.common.symbol.SymbolInterface;

public class RingsCPActivation extends Activation<SymbolInterface> {
    public RingsCPActivation(SymbolInterface textureKey, long stateChange, boolean dim) {
        super(textureKey, stateChange, dim);
    }

    @Override
    protected float getMaxStage() {
        return 5;
    }

    @Override
    protected float getTickMultiplier() {
        return 1.0F;
    }
}
