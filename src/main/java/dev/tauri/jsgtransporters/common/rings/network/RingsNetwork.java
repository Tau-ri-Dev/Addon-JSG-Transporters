package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class RingsNetwork extends SavedData {
    public static RingsNetwork INSTANCE = new RingsNetwork();
    public static final String DATA_NAME = JSGTransporters.MOD_ID + "_transport_rings";

    public void register(@Nonnull DimensionDataStorage storage) {
        INSTANCE = this;
        storage.computeIfAbsent(INSTANCE::load, () -> INSTANCE, DATA_NAME);
    }

    public RingsNetwork() {
    }

    public void removeRings(BlockPos pos) {
    }


    // ---------------------------------------------------------------------------------------------------------
    // Reading and writing

    public RingsNetwork load(CompoundTag compound) {
        // create new - clear old data
        INSTANCE.fromNBT(compound);
        return INSTANCE;
    }

    public void fromNBT(CompoundTag compound) {
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        return pCompoundTag;
    }

}
