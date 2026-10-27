package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.RegistryHelper;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.client.screen.RingsGui;
import dev.tauri.jsgtransporters.common.inventory.RingsContainer;
import net.minecraft.world.inventory.MenuType;

public class JSGTMenuTypes {
    public static final RegistryObjectWrapper<MenuType<RingsContainer>> RINGS_MENU_TYPE = JSGTransporters.REGISTRY_HELPER.menu().register("rings_container", RegistryHelper.menu(RingsContainer::new));

    public static void init() {
        JSGTransporters.REGISTRY_HELPER.guiRegister(() -> {
            RegistryHelper.bindScreenToMenu(RINGS_MENU_TYPE.get(), RingsGui::new);
        });
    }
}
