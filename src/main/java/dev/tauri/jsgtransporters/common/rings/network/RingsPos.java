package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.chunkloader.ChunkManager;
import dev.tauri.jsg.helpers.DimensionsHelper;
import dev.tauri.jsg.stargate.network.SymbolTypeEnum;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RingsPos implements INBTSerializable<CompoundTag> {
    public ResourceKey<Level> dimension;
    public BlockPos ringsPos;
    private SymbolTypeEnum<?> symbolType;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public RingsPos(ResourceKey<Level> dimension, BlockPos ringsPos, SymbolTypeEnum<?> symbolType) {
        this.dimension = dimension;
        this.ringsPos = ringsPos;
        this.symbolType = symbolType;
    }

    public RingsPos(CompoundTag compound) {
        this.deserializeNBT(compound);
    }

    public RingsPos(ByteBuf buf) {
        this.fromBytes(new FriendlyByteBuf(buf));
    }

    public SymbolTypeEnum<?> getSymbolType() {
        if (this.symbolType != null) {
            return this.symbolType;
        } else {
            this.symbolType = this.getBlockEntity().getSymbolType();
            return this.symbolType;
        }
    }

    public Level getWorld() {
        return Objects.requireNonNull(DimensionsHelper.getLevel(dimension));
    }

    public RingsAbstractBE getBlockEntity() {
        try {
            BlockEntity tile = getWorld().getBlockEntity(ringsPos);
            if (tile == null) {
                ChunkManager.forceChunk((ServerLevel) getWorld(), new ChunkPos(ringsPos));
                tile = getWorld().getBlockEntity(ringsPos);
                ChunkManager.unforceChunk((ServerLevel) getWorld(), new ChunkPos(ringsPos));
            }

            return (RingsAbstractBE) tile;
        } catch (Exception e) {
            JSGTransporters.logger.error("Error while getting tile entity from Rings pos!", e);
            return null;
        }
    }

    public BlockState getBlockState() {
        return this.getWorld().getBlockState(ringsPos);
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("dim", this.dimension.location().toString());
        compound.putLong("pos", ringsPos.asLong());
        compound.putString("name", this.name == null ? "" : this.name);
        if (this.symbolType != null) {
            compound.putString("symbolType", symbolType.getId());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compound.getString("dim")));
        this.ringsPos = BlockPos.of(compound.getLong("pos"));
        this.name = compound.getString("name");
        if (compound.contains("symbolType")) {
            this.symbolType = SymbolTypeEnum.byId(compound.getString("symbolType"));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceKey(this.dimension);
        buf.writeLong(this.ringsPos.asLong());
        if (this.name != null) {
            buf.writeBoolean(true);
            buf.writeInt(this.name.length());
            buf.writeCharSequence(this.name, StandardCharsets.UTF_8);
        } else {
            buf.writeBoolean(false);
        }

        if (this.symbolType != null) {
            buf.writeBoolean(true);
            buf.writeInt(SymbolTypeEnum.getId(this.symbolType));
        } else {
            buf.writeBoolean(false);
        }
    }

    public void fromBytes(FriendlyByteBuf buf) {
        this.dimension = buf.readResourceKey(Registries.DIMENSION);
        this.ringsPos = BlockPos.of(buf.readLong());
        if (buf.readBoolean()) {
            int nameSize = buf.readInt();
            this.name = buf.readCharSequence(nameSize, StandardCharsets.UTF_8).toString();
        }

        if (buf.readBoolean()) {
            this.symbolType = SymbolTypeEnum.byId(buf.readInt());
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // Hashing

    @Override
    public String toString() {
        return String.format("[dim=%s, pos=%s, name=%s]", dimension.location(), ringsPos.toString(), getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dimension.hashCode();
        result = prime * result + ((ringsPos == null) ? 0 : ringsPos.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RingsPos other = (RingsPos) obj;
        if (dimension != other.dimension)
            return false;
        if (ringsPos == null) {
            return other.ringsPos == null;
        } else return ringsPos.equals(other.ringsPos);
    }
}
