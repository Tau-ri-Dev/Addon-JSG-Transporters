package dev.tauri.jsgtransporters;

import dev.tauri.jsg.helpers.TabHelper;
import dev.tauri.jsg.helpers.registry.item.ItemRegistryHelperGeneric;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;

public class Constants {
    // helpers
    public static final ItemRegistryHelperGeneric ITEM_HELPER = new ItemRegistryHelperGeneric(() -> ItemRegistry.REGISTER);
    public static final TabHelper TAB_HELPER = new TabHelper(() -> TabRegistry.REGISTER);
}
