package dev.tauri.jsgtransporters.common.activation;


import dev.tauri.jsgcore.client.renderer.Activation;
import dev.tauri.jsgcore.common.symbol.SymbolInterface;

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
