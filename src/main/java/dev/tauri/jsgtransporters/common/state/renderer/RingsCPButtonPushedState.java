package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsg.state.State;
import io.netty.buffer.ByteBuf;

public class RingsCPButtonPushedState extends State {
    public int symbolId;
    public int symbolType;
    public boolean dim = false;

    public RingsCPButtonPushedState() {
    }

    public RingsCPButtonPushedState(boolean clear) {
        dim = clear;
    }

    public RingsCPButtonPushedState(SymbolInterface symbol) {
        symbolId = symbol.getId();
        symbolType = SymbolTypeEnum.getId(symbol.getSymbolType());
    }

    public SymbolInterface getSymbol() {
        return SymbolTypeEnum.byId(symbolType).valueOf(symbolId);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(symbolId);
        buf.writeInt(symbolType);
        buf.writeBoolean(dim);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        symbolId = buf.readInt();
        symbolType = buf.readInt();
        dim = buf.readBoolean();
    }
}
