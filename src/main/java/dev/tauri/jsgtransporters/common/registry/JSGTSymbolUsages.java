package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.symbol.SymbolUsage;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraftforge.registries.RegistryObject;

public class JSGTSymbolUsages {
    public static final RegistryObject<SymbolUsage> RINGS = JSGTransporters.REGISTRY_HELPER.symbolUsage().register("rings", () -> new SymbolUsage("rings", JSGTNotebookPageTypes.RINGS_ADDRESS::get));

    public static void init() {
    }
}
