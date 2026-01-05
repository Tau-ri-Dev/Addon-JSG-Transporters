package dev.tauri.jsgtransporters.common.state.gui;

import dev.tauri.jsg.api.config.ingame.BEConfig;
import dev.tauri.jsg.api.stargate.network.address.symbol.types.AbstractSymbolType;
import dev.tauri.jsg.api.state.State;
import dev.tauri.jsgtransporters.common.rings.network.AddressTypeRegistry;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddress;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class RingsContainerGuiState extends State {
    public RingsContainerGuiState(BEConfig config) {
        this.config = config;
    }

    public Map<AbstractSymbolType<?>, RingsAddress> addressMap = new HashMap<>();
    public final BEConfig config;

    public RingsContainerGuiState(Map<AbstractSymbolType<?>, RingsAddress> addressMap, BEConfig config) {
        this.addressMap = addressMap;
        this.config = config;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (AbstractSymbolType<?> symbolType : AbstractSymbolType.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            addressMap.get(symbolType).toBytes(buf);
        }

        config.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        addressMap = new HashMap<>(3);

        for (AbstractSymbolType<?> symbolType : AbstractSymbolType.values(AddressTypeRegistry.RINGS_SYMBOLS)) {
            var address = new RingsAddress(symbolType);
            address.fromBytes(buf);
            addressMap.put(symbolType, address);
        }

        config.fromBytes(buf);
    }
}
