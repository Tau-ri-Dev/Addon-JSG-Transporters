package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.api.stargate.network.address.symbol.types.AbstractSymbolType;

import static dev.tauri.jsg.api.stargate.network.address.symbol.SymbolTypeRegistry.registerSymbolType;

public class SymbolTypeRegistry {
    public static final AbstractSymbolType<SymbolGoauldEnum> GOAULD = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolGoauldEnum.Provider());
    public static final AbstractSymbolType<SymbolAncientEnum> ANCIENT = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolAncientEnum.Provider());
    public static final AbstractSymbolType<SymbolOriEnum> ORI = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolOriEnum.Provider());


    public static void register() {
    }
}
