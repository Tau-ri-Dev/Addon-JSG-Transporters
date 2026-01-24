package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.api.helper.DimensionsHelper;
import dev.tauri.jsg.api.stargate.network.address.symbol.types.AbstractSymbolType;
import dev.tauri.jsg.config.stargate.StargateDimensionConfig;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import javax.annotation.Nonnull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RingsNetwork extends SavedData {
    public static RingsNetwork INSTANCE = new RingsNetwork();
    public static final String DATA_NAME = JSGTransporters.MOD_ID + "_transport_rings";

    public void register(@Nonnull DimensionDataStorage storage) {
        INSTANCE = this;
        storage.computeIfAbsent(INSTANCE::load, () -> INSTANCE, DATA_NAME);
    }

    public RingsNetwork() {
    }

    private final Map<RingsPos, Map<AbstractSymbolType<?>, RingsAddress>> RINGS_MAP_BY_POS = new LinkedHashMap<>();
    private final Map<RingsAddress, RingsPos> RINGS_MAP_BY_ADDRESS = new LinkedHashMap<>();

    public Map<RingsPos, Map<AbstractSymbolType<?>, RingsAddress>> getAll() {
        return RINGS_MAP_BY_POS;
    }

    @Nullable
    public RingsPos getRings(RingsAddress address) {
        if (address == null)
            return null;
        if (address.getSize() < 4)
            return null;

        RingsPos pos = RINGS_MAP_BY_ADDRESS.get(address);
        if (pos != null && pos.getWorld() == null) return null;
        return pos;
    }

    @Nullable
    public Map<AbstractSymbolType<?>, RingsAddress> getAddresses(RingsPos pos) {
        if (pos == null) return null;
        var m = RINGS_MAP_BY_POS.get(pos);
        if (m == null) return null;
        return new HashMap<>(m);
    }

    public void removeRings(RingsPos pos) {
        if (pos == null) return;
        JSGTransporters.logger.info("Removing rings addresses at {} from the network!", pos);

        RINGS_MAP_BY_POS.remove(pos);

        for (var e : new ArrayList<>(RINGS_MAP_BY_ADDRESS.entrySet())) {
            if (e.getValue() == pos) {
                RINGS_MAP_BY_ADDRESS.remove(e.getKey());
            }
        }

        setDirty();
    }

    public List<RingsAddress> getAllAddresses() {
        return new ArrayList<>(RINGS_MAP_BY_ADDRESS.keySet());
    }

    public void putRings(RingsAddress address, RingsPos pos) {
        var map = new HashMap<AbstractSymbolType<?>, RingsAddress>();
        map.put(address.getSymbolType(), address);
        putRings(map, pos);
    }

    public void putRings(Map<AbstractSymbolType<?>, RingsAddress> addressMap, RingsPos ringsPos) {
        if (addressMap == null) {
            JSGTransporters.logger.warn("Tried to add NULL-address gate! Aborting...", new NullPointerException());
            return;
        }

        var map = RINGS_MAP_BY_POS.get(ringsPos);
        if (map == null)
            RINGS_MAP_BY_POS.put(ringsPos, new HashMap<>(addressMap));
        else {
            map.putAll(addressMap);
            RINGS_MAP_BY_POS.put(ringsPos, map);
        }
        for (var address : addressMap.values()) {
            RINGS_MAP_BY_ADDRESS.put(address, ringsPos);
        }
        checkForInvalidDims();
        setDirty();
    }

    private void checkForInvalidDims() {
        if (JSG.currentServer == null) return; // we are on client - do not check
        var map = new HashMap<>(RINGS_MAP_BY_POS);
        for (var e : map.entrySet()) {
            var pos = e.getKey();
            if (DimensionsHelper.getLevel(pos.dimension) == null) {
                JSGTransporters.logger.info("Removing rings at {} from the network as dim is INVALID!", pos);
                removeRings(pos);
            }
        }
    }

    public void renameRings(RingsPos pos, String newName) {
        var map = getAddresses(pos);
        removeRings(pos);
        JSGTransporters.logger.info("Setting rings name at {} to: {}", pos, newName);
        pos.setName(newName);
        putRings(map, pos);
        setDirty();
    }

    public boolean isOutOfRange(RingsPos outgoing, RingsPos incoming, boolean hasDimUpgrade) {
        if (outgoing == null || incoming == null) return true;
        var pos = outgoing.ringsPos.getCenter().add(0, -outgoing.ringsPos.getCenter().y(), 0);
        var targetPos = incoming.ringsPos.getCenter().add(0, -incoming.ringsPos.getCenter().y(), 0);
        var dim = outgoing.dimension;
        var targetDim = incoming.dimension;

        var horizontalRange = JSGTConfig.General.ringsRange.get();
        var dimRange = JSGTConfig.General.ringsRangeInterDim.get();

        if (dim != targetDim) {
            if (!hasDimUpgrade) return true;
            var sourceEntry = StargateDimensionConfig.INSTANCE.getConfigEntry(dim);
            var targetEntry = StargateDimensionConfig.INSTANCE.getConfigEntry(targetDim);
            if (sourceEntry == null || targetEntry == null) return true;

            var distance = Math.abs(sourceEntry.distance - targetEntry.distance);
            if (distance > dimRange) return true;

            if (targetDim == Level.NETHER)
                pos = pos.multiply(1f / 8f, 1f / 8f, 1f / 8f);
            if (dim == Level.NETHER)
                pos = pos.multiply(8f, 8f, 8f);
        }
        var distanceHorizontal = pos.distanceTo(targetPos);
        return distanceHorizontal > horizontalRange;
    }

    @Nullable
    public RingsPos getNearestRings(RingsPos source) {
        RingsPos nearest = null;
        double distance = Double.MAX_VALUE;
        for (var entry : RINGS_MAP_BY_POS.entrySet()) {
            var pos = entry.getKey();
            if (pos.dimension != source.dimension) continue;
            if (pos.ringsPos == source.ringsPos || Math.sqrt(source.ringsPos.distSqr(pos.ringsPos)) < 3) continue;
            var newDist = pos.ringsPos.distSqr(source.ringsPos);
            if (newDist > distance) continue;
            if (isOutOfRange(source, pos, false)) continue;
            distance = newDist;
            nearest = pos;
        }
        return nearest;
    }


    // ---------------------------------------------------------------------------------------------------------
    // Reading and writing

    public RingsNetwork load(CompoundTag compound) {
        // create new - clear old data
        INSTANCE.fromNBT(compound);
        return INSTANCE;
    }

    public void fromNBT(CompoundTag compound) {
        RINGS_MAP_BY_POS.clear();
        RINGS_MAP_BY_ADDRESS.clear();
        ListTag ringsTagList = compound.getList("rings", Tag.TAG_COMPOUND);

        for (Tag ringsTag : ringsTagList) {
            CompoundTag ringsCompound = (CompoundTag) ringsTag;

            RingsPos ringsPos = new RingsPos(ringsCompound.getCompound("pos"));
            var tagMap = ringsCompound.getList("addressMap", Tag.TAG_COMPOUND);
            for (var addressTag : tagMap) {
                putRings(new RingsAddress((CompoundTag) addressTag), ringsPos);
            }
        }
    }

    @Override
    public @Nonnull CompoundTag save(@Nonnull CompoundTag compound) {
        JSGTransporters.logger.info("Saving RINGS NETWORK: Started");
        ListTag ringsTagList = new ListTag();

        for (var rings : RINGS_MAP_BY_POS.entrySet()) {
            var ringsTag = new CompoundTag();
            ringsTag.put("pos", rings.getKey().serializeNBT());
            var mapList = new ListTag();
            for (var address : rings.getValue().entrySet()) {
                mapList.add(address.getValue().serializeNBT());
            }
            ringsTag.put("addressMap", mapList);
            ringsTagList.add(ringsTag);
        }
        compound.put("rings", ringsTagList);
        JSGTransporters.logger.info("Saving RINGS NETWORK: Done");
        return compound;
    }


    public void toBytes(ByteBuf buff) {
        FriendlyByteBuf buf = new FriendlyByteBuf(buff);
        // Write addresses
        buf.writeInt(RINGS_MAP_BY_POS.size());
        for (var rings : RINGS_MAP_BY_POS.entrySet()) {
            rings.getKey().toBytes(buf);
            buf.writeInt(rings.getValue().size());
            for (var address : rings.getValue().values()) {
                address.toBytes(buf);
            }
        }
    }

    public void fromBytes(ByteBuf buf) {
        // Read addresses
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            var pos = new RingsPos(buf);
            var addSize = buf.readInt();
            for (int j = 0; j < addSize; j++) {
                putRings(new RingsAddress(buf), pos);
            }
        }
    }
}
