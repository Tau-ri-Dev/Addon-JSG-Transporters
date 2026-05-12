package dev.tauri.jsgtransporters;

import dev.tauri.jsg.core.common.registry.helper.builder.item.ItemRegistryHelperGeneric;

public class Constants {
    public static final ItemRegistryHelperGeneric ITEM_HELPER = new ItemRegistryHelperGeneric(() -> ItemRegistry.REGISTER);

    public static void init() {
    }
}
