package dev.tauri.jsgtransporters;

import dev.tauri.jsg.api.client.LoadersHolder;
import dev.tauri.jsg.helpers.TabHelper;
import dev.tauri.jsg.helpers.registry.item.ItemRegistryHelperGeneric;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;

public class Constants {
    // contains instances of model and texture loaders for this addon
    public static final LoadersHolder LOADERS_HOLDER = LoadersHolder.getOrCreate(JSGTransporters.MOD_ID, JSGTransporters.class);

    // helpers
    public static final ItemRegistryHelperGeneric ITEM_HELPER = new ItemRegistryHelperGeneric(() -> ItemRegistry.REGISTER);
    public static final TabHelper TAB_HELPER = new TabHelper(() -> TabRegistry.REGISTER);


    public static void load() {
    }
}
