package dev.tauri.jsgtransporters.common.lantean_transporter.network;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.annotation.Nonnull;

import dev.tauri.jsgcore.common.chunkloader.ChunkManager;
import dev.tauri.jsgcore.common.helper.DimensionsHelper;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.lantean_transporter.LanteanTransporterBE;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class LanteanTransporterPos implements INBTSerializable<CompoundTag>, Comparable<LanteanTransporterPos> {
    public ResourceKey<Level> dimension;
    public BlockPos transporterPos;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public LanteanTransporterPos(ResourceKey<Level> dimension, BlockPos transporterPos) {
        this.dimension = dimension;
        this.transporterPos = transporterPos;
    }

    public LanteanTransporterPos(CompoundTag compound) {
        this.deserializeNBT(compound);
    }

    public LanteanTransporterPos(ByteBuf buf) {
        this.fromBytes(new FriendlyByteBuf(buf));
    }

    public Level getWorld() {
        return Objects.requireNonNull(DimensionsHelper.getLevel(dimension));
    }

    public LanteanTransporterBE getBlockEntity() {
        try {
            BlockEntity be = getWorld().getBlockEntity(transporterPos);
            if (be == null) {
                ChunkManager.forceChunk((ServerLevel)getWorld(), transporterPos);
                be = getWorld().getBlockEntity(transporterPos);
                ChunkManager.unforceChunk(getWorld(), transporterPos);
            }
            return (LanteanTransporterBE) be;
        } catch (ClassCastException e) {
            JSGTransporters.logger.error("Error while getting tile entity from lantean transporter pos!", e);
            return null;
        }
    }

    public BlockState getBlockState() {
        return getWorld().getBlockState(transporterPos);
    }

    @Override
    public @Nonnull CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("dim", dimension.location().toString());
        compound.putLong("pos", transporterPos.asLong());
        if (name != null) {
            compound.putString("name", name);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.dimension = ResourceKey.create(Registries.DIMENSION, JSGMapping.rl(nbt.getString("dim")));
        this.transporterPos = BlockPos.of(nbt.getLong("pos"));
        if (nbt.contains("name")) {
            this.name = nbt.getString("name");
        } else {
            this.name = null;
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceKey(this.dimension);
        buf.writeLong(this.transporterPos.asLong());
        if (this.name != null) {
            buf.writeBoolean(true);
            buf.writeUtf(this.name);
        } else {
            buf.writeBoolean(false);
        }
    }

    public void fromBytes(FriendlyByteBuf buf) {
        this.dimension = buf.readResourceKey(Registries.DIMENSION);
        this.transporterPos = BlockPos.of(buf.readLong());
        if (buf.readBoolean()) {
            this.name = buf.readUtf();
        } else {
            this.name = null;
        }
    }

    @Override
    public int compareTo(LanteanTransporterPos other) {
        int dimensionComparison = dimension.location().toString().compareTo(other.dimension.location().toString());
        if (dimensionComparison != 0) {
            return dimensionComparison;
        }
        return Long.compare(transporterPos.asLong(), other.transporterPos.asLong());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LanteanTransporterPos)) {
            return false;
        }
        LanteanTransporterPos pos = (LanteanTransporterPos) other;
        return Objects.equals(dimension, pos.dimension) && Objects.equals(transporterPos, pos.transporterPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, transporterPos);
    }
    
}
