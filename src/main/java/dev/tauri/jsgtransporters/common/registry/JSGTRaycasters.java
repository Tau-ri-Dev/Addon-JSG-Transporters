package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.raycaster.AncientCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.GoauldCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.OriCPRaycaster;
import net.minecraftforge.registries.RegistryObject;

public class JSGTRaycasters {
    public static final RegistryObject<AncientCPRaycaster> ANCIENT_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("ancient_cp", AncientCPRaycaster::new);
    public static final RegistryObject<GoauldCPRaycaster> GOAULD_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("goauld_cp", GoauldCPRaycaster::new);
    public static final RegistryObject<OriCPRaycaster> ORI_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("ori_cp", OriCPRaycaster::new);

    public static void init() {
    }
}
