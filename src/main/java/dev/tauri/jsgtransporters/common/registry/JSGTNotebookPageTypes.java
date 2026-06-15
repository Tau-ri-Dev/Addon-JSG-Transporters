package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.entity.AddressNotebookPageType;
import dev.tauri.jsg.core.common.entity.NotebookPageType;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.entity.RingsAddressData;
import net.minecraftforge.registries.RegistryObject;

public class JSGTNotebookPageTypes {
    public static final RegistryObject<NotebookPageType<RingsAddressData>> RINGS_ADDRESS = JSGTransporters.REGISTRY_HELPER.notebookPage().register("rings_address", () -> new AddressNotebookPageType<>(
            RingsAddressData::new,
            ringsAddressData -> ringsAddressData != null ? ringsAddressData.serializeNBT() : null,
            (level, pos, random, data) -> null // we don't want to generate addresses on cartouches now
    ));

    public static void init() {
    }
}
