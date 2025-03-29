package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JSGTransporters.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB_RINGS = Constants.TAB_HELPER.createCreativeTab("rings", () -> BlockRegistry.RING_GOAULD);


    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
