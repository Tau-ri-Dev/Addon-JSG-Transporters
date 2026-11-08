package dev.tauri.jsgtransporters.common.energy;

import dev.tauri.jsgcore.common.config.JSGCoreConfig;
import dev.tauri.jsgcore.common.power.general.LargeEnergyStorage;

public class PowerUtils {
    public static LargeEnergyStorage getRings(Runnable onChanged) {
        return new LargeEnergyStorage(JSGCoreConfig.Energy.basicEnergyCrystalCapacity.get(), JSGCoreConfig.Energy.basicEnergyCrystalEnergyReceiveSpeed.get()) {
            @Override
            public void onEnergyChanged() {
                onChanged.run();
            }
        };
    }

    public static EnergyRequiredToOperateRings ringsConsumption() {
        return EnergyRequiredToOperateRings.rings();
    }
}
