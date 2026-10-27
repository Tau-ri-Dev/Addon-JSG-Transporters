package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.common.symbol.SymbolUsage;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTSymbolUsages {
    public static final RegistryObjectWrapper<SymbolUsage> RINGS = JSGTransporters.REGISTRY_HELPER.symbolUsage().register("rings", () -> new SymbolUsage("rings", JSGTNotebookPageTypes.RINGS_ADDRESS::get));

    public static void init() {
    }
}
