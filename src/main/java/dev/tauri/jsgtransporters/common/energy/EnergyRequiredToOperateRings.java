package dev.tauri.jsgtransporters.common.energy;

import dev.tauri.jsg.power.general.EnergyRequiredToOperate;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EnergyRequiredToOperateRings extends EnergyRequiredToOperate {
    public EnergyRequiredToOperateRings(int energyToOpen, int keepAlive) {
        super(energyToOpen, keepAlive);
    }

    public EnergyRequiredToOperateRings(double energyToOpen, double keepAlive) {
        super(energyToOpen, keepAlive);
    }

    public static EnergyRequiredToOperateRings rings() {
        return new EnergyRequiredToOperateRings(JSGTConfig.Energy.ringsStartEnergy.get(), JSGTConfig.Energy.ringsTransportEnergy.get());
    }

    public int getEnergyForTransport(Object object) {
        if (object instanceof LivingEntity)
            return keepAlive;
        if (object instanceof Entity)
            return (int) (keepAlive * 0.3);
        if (object instanceof TeleportHelper.BlockToTeleport btt)
            return (int) (keepAlive * btt.getEnergyCoefficient());
        return keepAlive;
    }

    public double getEnergyToStart() {
        return energyToOpen;
    }
}
