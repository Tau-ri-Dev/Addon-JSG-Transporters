package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.symbol.SymbolType;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.rings.network.SymbolAncientEnum;
import dev.tauri.jsgtransporters.common.rings.network.SymbolGoauldEnum;
import dev.tauri.jsgtransporters.common.rings.network.SymbolOriEnum;
import net.minecraftforge.registries.RegistryObject;

public class JSGTSymbolTypes {
    public static final RegistryObject<SymbolType<SymbolGoauldEnum>> GOAULD = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("goauld", SymbolGoauldEnum.Provider::new);
    public static final RegistryObject<SymbolType<SymbolAncientEnum>> ANCIENT = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("ancient", SymbolAncientEnum.Provider::new);
    public static final RegistryObject<SymbolType<SymbolOriEnum>> ORI = JSGTransporters.REGISTRY_HELPER.symbolType()
            .register("ori", SymbolOriEnum.Provider::new);

    public static void init() {
    }
}
