package dev.tauri.jsgtransporters.client.listener;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.item.tooltip.ClientRingsInventoryTooltip;
import dev.tauri.jsgtransporters.common.item.tooltip.ServerRingsInventoryTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JSGTransporters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModListener {
    @SubscribeEvent
    public static void registerItemStackRenderableTooltips(RegisterClientTooltipComponentFactoriesEvent e) {
        e.register(ServerRingsInventoryTooltip.class, ClientRingsInventoryTooltip::new);
    }
}
