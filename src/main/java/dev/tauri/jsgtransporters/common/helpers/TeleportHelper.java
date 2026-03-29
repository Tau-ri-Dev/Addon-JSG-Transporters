package dev.tauri.jsgtransporters.common.helpers;

import dev.tauri.jsg.helpers.FluidHelper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import javax.annotation.Nonnull;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

public class TeleportHelper {
    public static Entity teleportEntity(Entity entity, RingsPos sourceRings, RingsPos targetRings) {
        Level world = entity.level();
        ResourceKey<Level> sourceDim = world.dimension();

        var offset = entity.position()
                .subtract(sourceRings.ringsPos.getCenter().add(0, sourceRings.getBlockEntity().getVerticalOffset(), 0));
        var targetPos = targetRings.ringsPos.getCenter()
                .add(0, targetRings.getBlockEntity().getVerticalOffset(), 0)
                .add(offset);

        if (sourceDim == targetRings.dimension) {
            setRotationAndPosition(entity, entity.getYHeadRot(), new Vector3d(targetPos.x, targetPos.y, targetPos.z));
            return entity;
        } else {
            // Keep players on the default dimension change path.
            if (entity instanceof ServerPlayer player) {
                return player.changeDimension(
                        Objects.requireNonNull(Objects.requireNonNull(entity.getServer()).getLevel(targetRings.dimension)),
                        new RingsTeleporter(new Vector3d(targetPos.x, targetPos.y, targetPos.z), entity.getYRot()));
            }

            // Rebuild non-player entity trees manually to preserve mounted passengers across dimensions.
            return manualTeleportEntityTree(entity, targetRings, new Vector3d(targetPos.x, targetPos.y, targetPos.z));
        }
    }

    private static Entity manualTeleportEntityTree(Entity entity, RingsPos targetRings, Vector3d targetPos) {
        var server = entity.getServer();
        if (server == null) return null;

        ServerLevel targetLevel = server.getLevel(targetRings.dimension);
        if (targetLevel == null) return null;

        // Detach passengers before recreating the root entity in the target dimension.
        var passengers = new ArrayList<>(entity.getPassengers());
        for (Entity passenger : passengers) {
            passenger.stopRiding();
        }

        Entity teleportedRoot = manualTeleportSingle(entity, targetLevel, targetPos);
        if (teleportedRoot == null) return null;

        // Recreate passengers recursively and attach them back to the teleported root.
        for (Entity passenger : passengers) {
            Entity teleportedPassenger = teleportPassenger(passenger, targetRings, targetPos);
            if (teleportedPassenger != null) {
                teleportedPassenger.startRiding(teleportedRoot, true);
            }
        }

        return teleportedRoot;
    }

    private static Entity teleportPassenger(Entity passenger, RingsPos targetRings, Vector3d targetPos) {
        if (passenger instanceof ServerPlayer player) {
            var server = player.getServer();
            if (server == null) return null;

            ServerLevel targetLevel = server.getLevel(targetRings.dimension);
            if (targetLevel == null) return null;

            return player.changeDimension(
                    targetLevel,
                    new RingsTeleporter(targetPos, player.getYRot()));
        }

        return manualTeleportEntityTree(passenger, targetRings, targetPos);
    }

    private static Entity manualTeleportSingle(Entity entity, ServerLevel targetLevel, Vector3d targetPos) {
        CompoundTag tag = new CompoundTag();
        if (!entity.save(tag)) return null;

        // Passengers are rebuilt manually to avoid duplicating nested entity trees.
        tag.remove("Passengers");

        // Remove UUID data before spawning a new entity instance.
        stripUuidsRecursive(tag);

        Entity spawnedEntity = EntityType.loadEntityRecursive(tag, targetLevel, loaded -> {
            loaded.moveTo(targetPos.x, targetPos.y, targetPos.z, entity.getYRot(), entity.getXRot());
            loaded.setDeltaMovement(entity.getDeltaMovement());
            loaded.setYHeadRot(entity.getYHeadRot());
            loaded.fallDistance = 0;
            return loaded;
        });

        if (spawnedEntity == null) return null;

        targetLevel.addDuringTeleport(spawnedEntity);

        // Remove only the current source entity. Passengers are handled separately.
        entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);

        return spawnedEntity;
    }

    private static void stripUuidsRecursive(CompoundTag tag) {
        tag.remove("UUID");
        tag.remove("UUIDMost");
        tag.remove("UUIDLeast");

        if (tag.contains("Passengers", Tag.TAG_LIST)) {
            ListTag passengers = tag.getList("Passengers", Tag.TAG_COMPOUND);
            for (int i = 0; i < passengers.size(); i++) {
                stripUuidsRecursive(passengers.getCompound(i));
            }
        }
    }

    public static void setRotationAndPosition(Entity entity, float yawRotated, Vector3d pos) {
        entity.teleportTo(pos.x, pos.y, pos.z);
        entity.setYHeadRot(yawRotated);
    }

    @Nonnull
    public static BlockState applyStateChanges(BlockState oldState) {
        final FluidState waterState = Blocks.WATER.defaultBlockState().getFluidState();
        FluidState fluidState = oldState.getFluidState();
        if (oldState.getBlock() instanceof LiquidBlock && fluidState.isSource()) {
            // temp
            JSGTransporters.logger.info("{} {}", fluidState.is(TagsRegistry.TRANSPORTER_FLUIDS), fluidState.getFluidType()); // temp
            Fluid fluid = fluidState.getType();
            if (fluid instanceof FlowingFluid flowFluid && switch (JSGTConfig.General.ringsFluidTreatmentMode.get()) {
                case Always -> true;
                case Never -> false;
                case ByTag -> fluidState.is(TagsRegistry.TRANSPORTER_FLUIDS);
                case ExcludeTag -> !fluidState.is(TagsRegistry.TRANSPORTER_FLUIDS);
            }) {
                FluidState newState = flowFluid.getFlowing().defaultFluidState().setValue(FlowingFluid.LEVEL, 7);
                return newState.createLegacyBlock();
            }
        } else if (oldState.hasProperty(BlockStateProperties.WATERLOGGED) &&
                oldState.getValue(BlockStateProperties.WATERLOGGED) &&
                switch (JSGTConfig.General.ringsFluidTreatmentMode.get()) {
                    case Always -> true;
                    case Never -> false;
                    case ByTag -> waterState.is(TagsRegistry.TRANSPORTER_FLUIDS);
                    case ExcludeTag -> !waterState.is(TagsRegistry.TRANSPORTER_FLUIDS);
                }) {
            oldState = oldState.setValue(BlockStateProperties.WATERLOGGED, false);
        }
        return oldState;
    }

    public static void teleportBlocks(Stream<Map.Entry<BlockPos, BlockPos>> poses, RingsAbstractBE sourceRings, RingsAbstractBE targetRings, ArrayList<BlockToTeleport> pistonHeads) {
        var toPlace = poses.map(pp -> {
            var localLevel = sourceRings.getLevelNonnull();
            var remoteLevel = targetRings.getLevelNonnull();
            var local = pp.getKey();
            var remote = pp.getValue();
            var localBlock = TeleportHelper.applyStateChanges(localLevel.getBlockState(local));
            var remoteBlock = TeleportHelper.applyStateChanges(targetRings.getLevelNonnull().getBlockState(remote));

            // map blocks
            var bttLocal = Optional.ofNullable(localLevel.getBlockEntity(local))
                    .map(net.minecraft.world.level.block.entity.BlockEntity::serializeNBT)
                    .<BlockToTeleport>map(nbt -> new BlockToTeleport.BlockEntity(localBlock, nbt, remote, remoteLevel))
                    .orElseGet(() -> {
                        if (localBlock.getBlock() == Blocks.PISTON || localBlock.getBlock() == Blocks.STICKY_PISTON)
                            return new BlockToTeleport.Piston(localBlock, remote, remoteLevel);
                        if (localBlock.getBlock() == Blocks.PISTON_HEAD)
                            return new BlockToTeleport.Void();
                        return new BlockToTeleport.Block(localBlock, remote, remoteLevel);
                    });
            var bttRemote = Optional.ofNullable(remoteLevel.getBlockEntity(remote))
                    .map(net.minecraft.world.level.block.entity.BlockEntity::serializeNBT)
                    .<BlockToTeleport>map(nbt -> new BlockToTeleport.BlockEntity(remoteBlock, nbt, local, localLevel))
                    .orElseGet(() -> {
                        if (remoteBlock.getBlock() == Blocks.PISTON || remoteBlock.getBlock() == Blocks.STICKY_PISTON)
                            return new BlockToTeleport.Piston(remoteBlock, local, localLevel);
                        if (remoteBlock.getBlock() == Blocks.PISTON_HEAD)
                            return new BlockToTeleport.Void();
                        return new BlockToTeleport.Block(remoteBlock, local, localLevel);
                    });

            // check energy
            var energyLocal = sourceRings.getEnergyStored();
            if (sourceRings.energyToOperate == null)
                return Map.entry(new BlockToTeleport.Void(), new BlockToTeleport.Void());
            var energyNeededLocal = sourceRings.energyToOperate.getEnergyForTransport(bttLocal);
            if (energyLocal < energyNeededLocal)
                return Map.entry(new BlockToTeleport.Void(), new BlockToTeleport.Void());

            var energyRemote = targetRings.getEnergyStored();
            if (targetRings.energyToOperate == null)
                return Map.entry(new BlockToTeleport.Void(), new BlockToTeleport.Void());
            var energyNeededRemote = targetRings.energyToOperate.getEnergyForTransport(bttRemote);
            if (energyRemote < energyNeededRemote)
                return Map.entry(new BlockToTeleport.Void(), new BlockToTeleport.Void());

            // remove blocks
            if (localBlock.getBlock() != Blocks.PISTON_HEAD || pistonHeads == null)
                bttLocal.removeLocal(local, localLevel);
            if (remoteBlock.getBlock() != Blocks.PISTON_HEAD || pistonHeads == null)
                bttRemote.removeLocal(remote, remoteLevel);
            return Map.entry(bttLocal, bttRemote);
        });
        // teleport blocks
        toPlace.forEach(pp -> {
            pp.getKey().placeOrAdd(pistonHeads);
            pp.getValue().placeOrAdd(pistonHeads);
        });
    }

    public sealed interface BlockToTeleport {
        int PLACE_FLAGS = 2 | 32 | 16 | 64;

        default void removeLocal(BlockPos pos, Level level) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), BlockToTeleport.PLACE_FLAGS);
        }

        void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads);

        double getEnergyCoefficient();

        record Block(BlockState state, BlockPos pos, Level level) implements BlockToTeleport {

            @Override
            public void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads) {
                if (pistonHeads != null) {
                    if (level.getBlockState(pos).is(Blocks.PISTON_HEAD)) {
                        pistonHeads.add(this);
                        return;
                    }
                }
                level().setBlock(pos, state, PLACE_FLAGS);
            }

            @Override
            public double getEnergyCoefficient() {
                if (state.isAir())
                    return 0;
                if (state.canBeReplaced())
                    return 0.5;
                if (FluidHelper.isLiquidBlock(state))
                    return 1.5;
                return 1.3;
            }
        }

        record Void() implements BlockToTeleport {

            @Override
            public void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads) {
            }

            @Override
            public double getEnergyCoefficient() {
                return 0;
            }
        }

        record Piston(BlockState state, BlockPos pos, Level level) implements BlockToTeleport {

            @Override
            public void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads) {
                Direction direction = null;
                BlockState headState = null;
                if (state.hasProperty(PistonBaseBlock.EXTENDED) && state.getValue(PistonBaseBlock.EXTENDED)) {
                    direction = state.getOptionalValue(DirectionalBlock.FACING).orElse(Direction.NORTH);
                    headState = Blocks.PISTON_HEAD.defaultBlockState()
                            .setValue(PistonHeadBlock.FACING, direction)
                            .setValue(PistonHeadBlock.TYPE, state.getBlock() == Blocks.STICKY_PISTON ? PistonType.STICKY : PistonType.DEFAULT);
                }
                if (pistonHeads != null) {
                    if (level.getBlockState(pos).is(Blocks.PISTON_HEAD)) {
                        pistonHeads.add(this);
                        return;
                    }
                    if (direction != null && level.getBlockState(pos.offset(direction.getNormal())).is(Blocks.PISTON_HEAD)) {
                        pistonHeads.add(this);
                        return;
                    }
                }
                level().setBlock(pos, state, PLACE_FLAGS);
                if (direction != null)
                    level().setBlock(pos.offset(direction.getNormal()), headState, PLACE_FLAGS);
            }

            @Override
            public double getEnergyCoefficient() {
                return 0;
            }
        }

        record BlockEntity(BlockState state, CompoundTag nbt, BlockPos pos, Level level) implements BlockToTeleport {
            @Override
            public void removeLocal(BlockPos pos, Level level) {
                var be = level.getBlockEntity(pos);
                if (be instanceof Container container) {
                    container.clearContent();
                } else if (be != null) {
                    be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((itemHandler) -> {
                        for (var slot = 0; slot < itemHandler.getSlots(); slot++) {
                            itemHandler.getStackInSlot(slot).setCount(0);
                        }
                    });
                }
                BlockToTeleport.super.removeLocal(pos, level);
            }

            @Override
            public void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads) {
                if (pistonHeads != null) {
                    if (level.getBlockState(pos).is(Blocks.PISTON_HEAD)) {
                        pistonHeads.add(this);
                        return;
                    }
                }
                level().setBlock(pos, state, PLACE_FLAGS);
                var entity = level.getBlockEntity(pos);
                if (entity == null) {
                    JSGTransporters.logger.error("Expected block entity at {} in {} but no block entity found", pos, level);
                    return;
                }
                entity.deserializeNBT(nbt);
                entity.setChanged();
            }

            @Override
            public double getEnergyCoefficient() {
                return 2.5f;
            }
        }
    }
}
