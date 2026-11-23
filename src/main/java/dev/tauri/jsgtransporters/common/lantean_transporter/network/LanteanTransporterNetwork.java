package dev.tauri.jsgtransporters.common.lantean_transporter.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class LanteanTransporterNetwork extends SavedData {
    public static LanteanTransporterNetwork INSTANCE = new LanteanTransporterNetwork();
    public static final String DATA_NAME = JSGTransporters.MOD_ID + "_lantean_transporters";

    public void register(@Nonnull  DimensionDataStorage storage) {
        INSTANCE = this;
        storage.computeIfAbsent(INSTANCE::load, () -> INSTANCE, DATA_NAME);
    }

    public LanteanTransporterNetwork() {
    }

    private final Map<UUID, Set<LanteanTransporterPos>> TRANSPORTERS_BY_NETWORK_UUID = new HashMap<>();
    private final Map<UUID, Map<LanteanTransporterPos, List<LanteanTransporterPos>>> TRANSPORTERS_CONNECTIONS_BY_NETWORK_UUID = new HashMap<>();

    public Map<UUID, List<LanteanTransporterPos>> getReachableByNetwork(LanteanTransporterPos pos, int maxDepth) {
        throw new UnsupportedOperationException("Unimplemented method 'getReachableByNetwork'");
    }

    public void putTransporter(LanteanTransporterPos pos) {
        throw new UnsupportedOperationException("Unimplemented method 'putTransporter'");
    }

    public void putTransporter(LanteanTransporterPos pos, UUID networkUUID) {
        throw new UnsupportedOperationException("Unimplemented method 'putTransporter'");
        // setDirty();
    }

    private void insertTransporter(LanteanTransporterPos pos, UUID networkUUID){
        TRANSPORTERS_BY_NETWORK_UUID.computeIfAbsent(networkUUID, k -> new HashSet<>()).add(pos);
    }

    private void insertTransporterConnection(LanteanTransporterPos pos, LanteanTransporterPos target, UUID networkUUID) {
        Optional.ofNullable(TRANSPORTERS_BY_NETWORK_UUID.get(networkUUID)).ifPresentOrElse(l -> {
            if (!l.contains(pos)) throw new IllegalArgumentException("Transporter " + pos + " is not registered in network " + networkUUID);
            if (!l.contains(target)) throw new IllegalArgumentException("Transporter " + target + " is not registered in network " + networkUUID);
        }, () -> {
            throw new IllegalArgumentException("Network " + networkUUID + " is not registered");
        });
        var network = TRANSPORTERS_CONNECTIONS_BY_NETWORK_UUID.computeIfAbsent(networkUUID, k -> new HashMap<>());
        network.computeIfAbsent(pos, k -> new LinkedList<>()).add(target);
        network.computeIfAbsent(target, k -> new LinkedList<>()).add(pos);
    }

    public void removeTransporter(LanteanTransporterPos pos) {
        removeTransporter(pos, true);
    }

    public void removeTransporter(LanteanTransporterPos pos, UUID networkUUID) {
        removeTransporter(pos, networkUUID, true);
    }

    public void removeTransporter(LanteanTransporterPos pos, boolean cleanUp) {
        throw new UnsupportedOperationException("Unimplemented method 'removeTransporter'");
    }

    public void removeTransporter(LanteanTransporterPos pos, UUID networkUUID, boolean cleanUp) {
        throw new UnsupportedOperationException("Unimplemented method 'removeTransporter'");
        // setDirty();
    }

    public void renameTransporter(LanteanTransporterPos pos, String newName) {
        var reachable = getReachableByNetwork(pos, 1);
        removeTransporter(pos, false);
        for (UUID networkUUID : reachable.keySet()) {
            insertTransporter(pos, networkUUID);
            for (LanteanTransporterPos target : reachable.get(networkUUID)) {
                insertTransporterConnection(pos, target, networkUUID);
            }
        }
        setDirty();
    }

    public LanteanTransporterNetwork load(CompoundTag compound) {
        INSTANCE.fromNBT(compound);
        return INSTANCE;
    }

    public void fromNBT(CompoundTag compound) {
        TRANSPORTERS_BY_NETWORK_UUID.clear();
        TRANSPORTERS_CONNECTIONS_BY_NETWORK_UUID.clear();
        var networksCompound = compound.getCompound("networks");
        networksCompound.getAllKeys().forEach(key -> {
            var networkUUID = UUID.fromString(key);
            var networkCompound = networksCompound.getCompound(key);

            if (networkCompound.contains("transporters", 9)) {
                var transporters = networkCompound.getList("transporters", 10);
                for (int i = 0; i < transporters.size(); i++) {
                    insertTransporter(new LanteanTransporterPos(transporters.getCompound(i)), networkUUID);
                }
            }

            if (networkCompound.contains("edges", 9)) {
                var edges = networkCompound.getList("edges", 10);
                for (int i = 0; i < edges.size(); i++) {
                    var edge = edges.getCompound(i);
                    var pos = new LanteanTransporterPos(edge.getCompound("pos"));
                    var target = new LanteanTransporterPos(edge.getCompound("target"));
                    if (!TRANSPORTERS_BY_NETWORK_UUID.getOrDefault(networkUUID, Set.of()).contains(pos)) {
                        insertTransporter(pos, networkUUID);
                    }
                    if (!TRANSPORTERS_BY_NETWORK_UUID.getOrDefault(networkUUID, Set.of()).contains(target)) {
                        insertTransporter(target, networkUUID);
                    }
                    insertTransporterConnection(pos, target, networkUUID);
                }
                return;
            }
        });
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        CompoundTag networksCompound = new CompoundTag();
        TRANSPORTERS_BY_NETWORK_UUID.keySet().stream().sorted().forEach(networkUUID -> {
            var networkCompound = new CompoundTag();
            var transporterList = new ListTag();
            TRANSPORTERS_BY_NETWORK_UUID.getOrDefault(networkUUID, Set.of()).stream()
                    .sorted()
                    .forEach(pos -> transporterList.add(pos.serializeNBT()));
            networkCompound.put("transporters", transporterList);

            var connections = TRANSPORTERS_CONNECTIONS_BY_NETWORK_UUID.getOrDefault(networkUUID, Map.of());
            var edgesList = new ListTag();
            var visited = new HashSet<LanteanTransporterPos>();
            for (var pos : TRANSPORTERS_BY_NETWORK_UUID.getOrDefault(networkUUID, Set.of()).stream()
                    .sorted()
                    .toList()) {
                for (var target : connections.getOrDefault(pos, List.of()).stream()
                        .sorted()
                        .toList()) {
                    if (!visited.contains(target)) {
                        var edgeCompound = new CompoundTag();
                        edgeCompound.put("pos", pos.serializeNBT());
                        edgeCompound.put("target", target.serializeNBT());
                        edgesList.add(edgeCompound);
                    }
                }
                visited.add(pos);
            }
            networkCompound.put("edges", edgesList);
            networksCompound.put(networkUUID.toString(), networkCompound);
        });
        pCompoundTag.put("networks", networksCompound);
        return pCompoundTag;
    }
}
