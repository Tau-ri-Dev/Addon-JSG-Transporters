package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.core.common.entity.State;
import dev.tauri.jsg.core.common.symbol.SymbolInterface;
import dev.tauri.jsg.core.common.symbol.SymbolType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class RingsCPButtonPushedState extends State {
    public int symbolId;
    public SymbolType<?> symbolType;
    public boolean dim = false;

    public RingsCPButtonPushedState() {
    }

    public RingsCPButtonPushedState(boolean clear) {
        dim = clear;
    }

    public RingsCPButtonPushedState(SymbolInterface symbol) {
        symbolId = symbol.getId();
        symbolType = symbol.getSymbolType();
    }

    public SymbolInterface getSymbol() {
        return symbolType.valueOf(symbolId);
    }

    @Override
    public void toBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        buf.writeInt(symbolId);
        buf.writeResourceLocation(symbolType.getId());
        buf.writeBoolean(dim);
    }

    @Override
    public void fromBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        symbolId = buf.readInt();
        symbolType = SymbolType.byId(buf.readResourceLocation());
        dim = buf.readBoolean();
    }
}
