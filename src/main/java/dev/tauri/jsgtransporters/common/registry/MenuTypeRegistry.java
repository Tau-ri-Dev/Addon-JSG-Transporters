package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JSGTransporters.MOD_ID);

    public static final RegistryObject<MenuType<RingsContainer>> RINGS_MENU_TYPE = REGISTER.register("rings_container",
            () -> IForgeMenuType.create(RingsContainer::new)
    );

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
