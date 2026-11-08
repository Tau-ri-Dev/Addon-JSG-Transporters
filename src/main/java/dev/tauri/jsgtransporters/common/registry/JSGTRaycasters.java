package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.raycaster.AncientCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.GoauldCPRaycaster;
import dev.tauri.jsgtransporters.common.raycaster.OriCPRaycaster;

public class JSGTRaycasters {
    public static final RegistryObjectWrapper<AncientCPRaycaster> ANCIENT_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("ancient_cp", AncientCPRaycaster::new);
    public static final RegistryObjectWrapper<GoauldCPRaycaster> GOAULD_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("goauld_cp", GoauldCPRaycaster::new);
    public static final RegistryObjectWrapper<OriCPRaycaster> ORI_CP_RAYCASTER = JSGTransporters.REGISTRY_HELPER.raycaster().register("ori_cp", OriCPRaycaster::new);

    public static void init() {
    }
}
