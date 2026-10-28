package dev.tauri.jsgtransporters.common.capability;

import dev.tauri.jsgcore.JSGCore;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = JSGTransporters.MOD_ID)
public class JSGTCapabilityRegistration {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (var holder : JSGTransporters.REGISTRY_HELPER.be().getEntries()) {
            BlockEntityType<?> type = holder.get();

            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, (be, side) -> {
                if (be instanceof RingsAbstractBE rings)
                    return rings.getEnergyStorage();
                return null;
            });

            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (be, side) -> {
                if (be instanceof RingsAbstractBE rings)
                    return rings.getInventory();
                return null;
            });

            // CC:Tweaked peripherals (no-op when CC is absent)
            JSGCore.ccWrapper.registerPeripheralBE(event, type);
            // OC:Rebooted peripherals
            JSGCore.ocWrapper.registerPeripheralBE(event, type);
        }
    }
}
