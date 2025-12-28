package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.api.stargate.network.address.symbol.SymbolInterface;
import dev.tauri.jsg.api.stargate.network.address.symbol.types.AbstractSymbolType;
import dev.tauri.jsgtransporters.JSGTransporters;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class RingsAddressDynamic extends RingsAddress {

    public RingsAddressDynamic(AbstractSymbolType<?> symbolType) {
        super(symbolType);
    }

    public RingsAddressDynamic(CompoundTag compound) {
        super(compound);
    }

    public RingsAddressDynamic(ByteBuf buf) {
        super(buf);
    }

    public RingsAddressDynamic(RingsAddress address) {
        super(address.symbolType);
        clear();
        addAll(address);
    }

    public RingsAddressDynamic(AbstractSymbolType<?> symbolType, List<SymbolInterface> symbols) {
        super(symbolType);
        clear();
        addAll(symbols);
    }

    @Override
    protected int getSavedSymbols() {
        return Math.min(addressSize, 5);
    }

    // ---------------------------------------------------------------------------------
    // Address

    public void addSymbol(SymbolInterface symbol) {
        if (address.size() == 5) {
            JSGTransporters.logger.error("Tried to add symbol to already full address");
            return;
        }

        address.add(symbol);
        addressSize += 1;
    }

    public void addAll(RingsAddress ringsAddress) {
        if (address.size() + ringsAddress.address.size() > 5) {
            JSGTransporters.logger.error("Tried to add symbols to already populated address");
            return;
        }

        address.addAll(ringsAddress.address);
        addressSize += ringsAddress.address.size();
    }

    public void addAll(List<SymbolInterface> ringsAddress) {
        if (address.size() + ringsAddress.size() > 5) {
            JSGTransporters.logger.error("Tried to add symbols to already populated address");
            return;
        }

        address.addAll(ringsAddress);
        addressSize += ringsAddress.size();
    }

    public void addOrigin() {
        if (symbolType.hasOrigin()) {
            addSymbol(symbolType.getOrigin());
        }
    }

    public void clear() {
        address.clear();
        addressSize = 0;
    }

    public int size() {
        return address.size();
    }

    public boolean contains(SymbolInterface symbol) {
        return address.contains(symbol);
    }

    @SuppressWarnings("all")
    public boolean validate() {
        return symbolType.validateDialedAddress(this);
    }

    public RingsAddress toImmutable() {
        RingsAddress stargateAddress = new RingsAddress(symbolType);
        var copy = new ArrayList<>(address);
        if (copy.size() > 4) copy.remove(4);
        stargateAddress.address.addAll(copy);
        return stargateAddress;
    }

    // ---------------------------------------------------------------------------------
    // Serialization

    private int addressSize;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = super.serializeNBT();

        compound.putInt("size", address.size());

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        addressSize = compound.getInt("size");

        super.deserializeNBT(compound);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(address.size());

        super.toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        addressSize = buf.readInt();

        super.fromBytes(buf);
    }
}
