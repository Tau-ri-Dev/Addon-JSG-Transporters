package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.common.symbol.SymbolType;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import dev.tauri.jsgtransporters.common.rings.network.SymbolOriEnum;

public class JSGTSymbolTypes {
    public static final RegistryObjectWrapper<SymbolType<SymbolGoauldEnum>> GOAULD = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("goauld", SymbolGoauldEnum.Provider::new);
    public static final RegistryObjectWrapper<SymbolType<SymbolAncientEnum>> ANCIENT = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("ancient", SymbolAncientEnum.Provider::new);
    public static final RegistryObjectWrapper<SymbolType<SymbolOriEnum>> ORI = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("ori", SymbolOriEnum.Provider::new);

    public static void init() {
    }
}
