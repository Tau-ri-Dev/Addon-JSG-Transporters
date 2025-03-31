package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.stargate.network.SymbolTypeEnum;

import static dev.tauri.jsg.stargate.network.SymbolTypeRegistry.registerSymbolType;

public class SymbolTypeRegistry {
    public static final SymbolTypeEnum<SymbolGoauldEnum> GOAULD = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolGoauldEnum.Provider());
    public static final SymbolTypeEnum<SymbolAncientEnum> ANCIENT = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolAncientEnum.Provider());
    public static final SymbolTypeEnum<SymbolOriEnum> ORI = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolOriEnum.Provider());


    public static void register() {
    }
}
