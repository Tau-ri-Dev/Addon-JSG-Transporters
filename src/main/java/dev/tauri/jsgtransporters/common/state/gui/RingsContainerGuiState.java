package dev.tauri.jsgtransporters.common.state.gui;

import dev.tauri.jsg.core.common.config.ingame.BEConfig;
import dev.tauri.jsg.core.common.entity.State;
import dev.tauri.jsg.core.common.symbol.SymbolType;
import dev.tauri.jsgtransporters.common.registry.JSGTSymbolUsages;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddress;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public class RingsContainerGuiState extends State {
    public RingsContainerGuiState(BEConfig config) {
        this.config = config;
    }

    public Map<SymbolType<?>, RingsAddress> addressMap = new HashMap<>();
    public final BEConfig config;

    public RingsContainerGuiState(Map<SymbolType<?>, RingsAddress> addressMap, BEConfig config) {
        this.addressMap = addressMap;
        this.config = config;
    }

    @Override
    public void toBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        for (SymbolType<?> symbolType : SymbolType.values(JSGTSymbolUsages.RINGS.get())) {
            addressMap.get(symbolType).toBytes(buf);
        }

        config.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        addressMap = new HashMap<>(3);

        for (SymbolType<?> symbolType : SymbolType.values(JSGTSymbolUsages.RINGS.get())) {
            var address = new RingsAddress(symbolType);
            address.fromBytes(buf);
            addressMap.put(symbolType, address);
        }

        config.fromBytes(buf);
    }
}
