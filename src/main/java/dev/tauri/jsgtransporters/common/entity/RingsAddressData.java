package dev.tauri.jsgtransporters.common.entity;

import dev.tauri.jsg.core.common.entity.IAddressNotebookPageData;
import dev.tauri.jsg.core.common.entity.INotebookPageData;
import dev.tauri.jsg.core.common.symbol.address.IAddress;
import dev.tauri.jsg.core.common.symbol.pointoforigin.PointOfOrigin;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddressDynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RingsAddressData implements INBTSerializable<CompoundTag>, IAddressNotebookPageData {
    public RingsAddressDynamic address;
    public int[] symbolsToDisplay;

    public RingsAddressData(RingsAddressDynamic address, List<Integer> symbolsToDisplay) {
        this(address, new int[symbolsToDisplay.size()]);
        for (int i = 0; i < symbolsToDisplay.size(); i++) {
            this.symbolsToDisplay[i] = symbolsToDisplay.get(i);
        }
    }

    public RingsAddressData(RingsAddressDynamic address, int[] symbolsToDisplay) {
        this.address = address;
        this.symbolsToDisplay = symbolsToDisplay;
    }

    public RingsAddressData(RingsAddressDynamic address) {
        this.address = address;
        this.symbolsToDisplay = new int[]{1, 2, 3, 4, 9};
    }

    public RingsAddressData(CompoundTag compound) {
        deserializeNBT(compound);
    }

    @Override
    public IAddress getAddress() {
        return address;
    }

    @Override
    public int[] getSymbolsToDisplay() {
        return symbolsToDisplay;
    }

    @Override
    public @Nullable PointOfOrigin getOrigin() {
        return null;
    }

    @Override
    public void setSymbolsToDisplay(int[] ints) {

    }

    @Override
    public void setOrigin(@Nullable PointOfOrigin pointOfOrigin) {

    }

    @Override
    public void setAddress(IAddress iAddress) {

    }

    @Override
    public <D extends INotebookPageData> void update(D d) {

    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
