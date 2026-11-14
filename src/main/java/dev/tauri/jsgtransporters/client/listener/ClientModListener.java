package dev.tauri.jsgtransporters.client.listener;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.item.tooltip.ClientRingsInventoryTooltip;
import dev.tauri.jsgtransporters.common.item.tooltip.ServerRingsInventoryTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(modid = JSGTransporters.MOD_ID, value = Dist.CLIENT)
public class ClientModListener {
    @SubscribeEvent
    public static void registerItemStackRenderableTooltips(RegisterClientTooltipComponentFactoriesEvent e) {
        e.register(ServerRingsInventoryTooltip.class, ClientRingsInventoryTooltip::new);
    }
}
