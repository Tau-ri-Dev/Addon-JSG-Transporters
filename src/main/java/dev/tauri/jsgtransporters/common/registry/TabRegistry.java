package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JSGTransporters.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB_RINGS = Constants.TAB_HELPER.createCreativeTabWithItemStack("rings", () -> new ItemStack(BlockRegistry.RINGS_GOAULD.get()));


    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
