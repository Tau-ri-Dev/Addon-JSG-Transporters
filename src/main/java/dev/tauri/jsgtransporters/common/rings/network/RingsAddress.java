package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.core.common.entity.NotebookPageType;
import dev.tauri.jsg.core.common.symbol.SymbolInterface;
import dev.tauri.jsg.core.common.symbol.SymbolType;
import dev.tauri.jsg.core.common.symbol.address.IAddress;
import dev.tauri.jsg.core.common.symbol.pointoforigin.PointOfOrigin;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.entity.RingsAddressData;
import dev.tauri.jsgtransporters.common.registry.JSGTNotebookPageTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class RingsAddress implements IAddress {

    public RingsAddress(SymbolType<?> symbolType) {
        this.symbolType = symbolType;
    }

    public RingsAddress(ByteBuf byteBuf) {
        fromBytes(byteBuf);
    }

    public RingsAddress(CompoundTag compound) {
        deserializeNBT(compound);
    }

    protected int getSavedSymbols() {
        return 4;
    }


    // ---------------------------------------------------------------------------------
    // Address
    protected SymbolType<?> symbolType;
    protected List<SymbolInterface> address = new ArrayList<>(4);

    @Override
    public SymbolType<?> getSymbolType() {
        return symbolType;
    }

    @Override
    @ParametersAreNonnullByDefault
    public CompoundTag getCompound(int[] symbolsToDisplay, ResourceKey<Biome> resourceKey, @Nullable PointOfOrigin pointOfOrigin) {
        return getNotebookPageType().createCompoundTag(new RingsAddressData(new RingsAddressDynamic(this), symbolsToDisplay), resourceKey);
    }

    @Override
    public NotebookPageType<RingsAddressData> getNotebookPageType() {
        return JSGTNotebookPageTypes.RINGS_ADDRESS.get();
    }

    @Override
    public IAddress addOriginIfMissingAndImmutable() {
        return Util.make(new RingsAddressDynamic(getSymbolType()), (a) -> {
            a.addAll(this.address);
            a.addOrigin();
        });
    }

    /**
     * Generates new random address.
     *
     * @param random {@link Random} instance.
     */
    public RingsAddress generate(Random random) {
        if (!address.isEmpty()) {
            JSGTransporters.logger.error("Tried to regenerate address already containing symbols", new ConcurrentModificationException());
            for (var s : address)
                JSGTransporters.logger.error(s.getEnglishName());
            return this;
        }

        while (address.size() < 4) {
            SymbolInterface symbol = symbolType.getRandomSymbol(random);
            address.add(symbol);
        }

        return this;
    }

    @Override
    public SymbolInterface get(int symbolIndex) {
        return address.get(symbolIndex);
    }

    public void set(int index, SymbolInterface symbol) {
        address.set(index, symbol);
    }

    public SymbolInterface getLast() {
        if (address.isEmpty())
            return null;

        return address.get(address.size() - 1);
    }

    public List<String> getNameList() {
        List<String> out = new ArrayList<>(address.size());

        for (SymbolInterface symbol : address) {
            out.add(symbol.getEnglishName());
        }

        return out;
    }

    public List<SymbolInterface> subList(int start, int end) {
        return address.subList(start, end);
    }

    @Override
    public int getSize() {
        return address.size();
    }


    // ---------------------------------------------------------------------------------
    // Serialization

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();

        compound.putString("symbolType", symbolType.getId().toString());

        for (int i = 0; i < getSavedSymbols(); i++)
            compound.putInt("symbol" + i, address.get(i).getId());

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        if (!address.isEmpty()) {
            JSGTransporters.logger.error("Tried to deserialize address already containing symbols", new ConcurrentModificationException());
            for (var s : address)
                JSGTransporters.logger.error(s.getEnglishName());
            return;
        }

        symbolType = SymbolType.byId(JSGMapping.rl(compound.getString("symbolType")));

        for (int i = 0; i < getSavedSymbols(); i++)
            address.add(symbolType.valueOf(compound.getInt("symbol" + i)));
    }

    public void toBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        buf.writeResourceLocation(symbolType.getId());

        for (int i = 0; i < getSavedSymbols(); i++)
            buf.writeByte(address.get(i).getId());
    }

    public void fromBytes(ByteBuf buff) {
        var buf = new FriendlyByteBuf(buff);
        if (!address.isEmpty()) {
            JSGTransporters.logger.error("Tried to deserialize address already containing symbols");
            return;
        }

        symbolType = SymbolType.byId(buf.readResourceLocation());

        for (int i = 0; i < getSavedSymbols(); i++)
            address.add(symbolType.valueOf(buf.readByte()));
    }


    // ---------------------------------------------------------------------------------
    // Hashing

    @Override
    public String toString() {
        var stringAddress = new StringBuilder();
        for (var symbol : address) {
            stringAddress.append(symbol.getEnglishName()).append(", ");
        }
        return "{symbolType: " + symbolType + ", address: [" + stringAddress + "]}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.subList(0, 4).hashCode());
        result = prime * result + ((symbolType == null) ? 0 : symbolType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RingsAddress other))
            return false;
        if (address == null)
            return other.address == null;
        if (address.size() < 4) return false;
        if (other.address.size() < 4) return false;
        if (!address.subList(0, 4).equals(other.address.subList(0, 4))) return false;
        return symbolType == other.symbolType;
    }
}
