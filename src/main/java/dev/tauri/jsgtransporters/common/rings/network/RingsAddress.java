package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.stargate.network.IAddress;
import dev.tauri.jsg.stargate.network.SymbolInterface;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import net.minecraft.nbt.CompoundTag;

public class RingsAddress implements IAddress {

    public RingsAddress(CompoundTag compoundTag) {
        deserializeNBT(compoundTag);
    }

    @Override
    public SymbolInterface get(int symbolIndex) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public SymbolTypeEnum<?> getSymbolType() {
        return null;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
