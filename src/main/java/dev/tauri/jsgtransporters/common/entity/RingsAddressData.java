package dev.tauri.jsgtransporters.common.entity;

import dev.tauri.jsg.core.common.entity.IAddressNotebookPageData;
import dev.tauri.jsg.core.common.entity.INotebookPageData;
import dev.tauri.jsg.core.common.symbol.address.IAddress;
import dev.tauri.jsg.core.common.symbol.pointoforigin.PointOfOrigin;
import dev.tauri.jsgtransporters.common.rings.network.RingsAddressDynamic;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
    public RingsAddressDynamic getAddress() {
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
    public void setSymbolsToDisplay(int[] symbolsToDisplay) {
        this.symbolsToDisplay = symbolsToDisplay;
    }

    @Override
    public void setOrigin(@Nullable PointOfOrigin pointOfOrigin) {

    }

    @Override
    public void setAddress(IAddress address) {
        if (address instanceof RingsAddressDynamic ringsAddress)
            this.address = ringsAddress;
    }

    @Override
    public <D extends INotebookPageData> void update(D newData) {
        if (!(newData instanceof IAddressNotebookPageData addressNotebookPageData)) return;
        this.symbolsToDisplay = addressNotebookPageData.getSymbolsToDisplay();
        if (!(newData instanceof RingsAddressData ringsAddressData)) return;
        this.address = ringsAddressData.getAddress();
    }

    @Override
    public CompoundTag serializeNBT() {
        var compound = new CompoundTag();
        compound.put("address", new RingsAddressDynamic(address).serializeNBT());
        compound.putIntArray("symbolsToDisplay", symbolsToDisplay);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        address = new RingsAddressDynamic(compound.getCompound("address"));
        symbolsToDisplay = compound.getIntArray("symbolsToDisplay");
    }
}
