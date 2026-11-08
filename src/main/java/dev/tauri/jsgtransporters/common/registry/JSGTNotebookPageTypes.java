package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.entity.AddressNotebookPageType;
import dev.tauri.jsgcore.common.entity.NotebookPageType;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.entity.RingsAddressData;

public class JSGTNotebookPageTypes {
    public static final RegistryObjectWrapper<NotebookPageType<RingsAddressData>> RINGS_ADDRESS = JSGTransporters.REGISTRY_HELPER.notebookPage().register("rings_address", () -> new AddressNotebookPageType<>(
            RingsAddressData::new,
            ringsAddressData -> ringsAddressData != null ? ringsAddressData.serializeNBT() : null,
            (level, pos, random, data) -> null // we don't want to generate addresses on cartouches now
    ));

    public static void init() {
    }
}
