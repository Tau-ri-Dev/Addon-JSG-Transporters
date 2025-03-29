package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.stargate.network.SymbolTypeEnum;

import static dev.tauri.jsg.stargate.network.SymbolTypeRegistry.registerSymbolType;

public class SymbolTypeRegistry {
    public static final SymbolTypeEnum<SymbolGoauldEnum> GOAULD = registerSymbolType(AddressTypeRegistry.RINGS_SYMBOLS, new SymbolGoauldEnum.Provider());


    public static void register(){}
}
