package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.registry.helper.RegistryHelper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.screen.RingsGui;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

public class JSGTMenuTypes {
    public static final RegistryObject<MenuType<RingsContainer>> RINGS_MENU_TYPE = JSGTransporters.REGISTRY_HELPER.menu().register("rings_container", RegistryHelper.menu(RingsContainer::new));

    public static void init() {
        JSGTransporters.REGISTRY_HELPER.guiRegister(() -> {
            RegistryHelper.bindScreenToMenu(RINGS_MENU_TYPE.get(), RingsGui::new);
        });
    }
}
