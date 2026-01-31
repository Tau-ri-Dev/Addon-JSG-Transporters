package dev.tauri.jsgtransporters.common.helpers;

import dev.tauri.jsg.api.util.RotationUtil;
import dev.tauri.jsg.helpers.FluidHelper;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.registry.TagsRegistry;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import javax.annotation.Nonnull;

import org.joml.Quaternionf;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TeleportHelper {
    public static void teleportEntity(Entity entity, RingsPos sourceRings, RingsPos targetRings) {
        Level world = entity.level();
        ResourceKey<Level> sourceDim = world.dimension();

        var offset = entity.position()
                .subtract(sourceRings.ringsPos.getCenter().add(0, sourceRings.getBlockEntity().getVerticalOffset(), 0));
        var tPos = targetRings.ringsPos.getCenter().add(0, targetRings.getBlockEntity().getVerticalOffset(), 0)
                .add(offset);

        if (sourceDim == targetRings.dimension) {
            setRotationAndPosition(entity, entity.getYHeadRot(), new Vector3d(tPos.x, tPos.y, tPos.z));
        } else {
            // if (!fireTravelToDimEvent(entity, targetRings.dimension))
            // return;

            entity.changeDimension(
                    Objects.requireNonNull(Objects.requireNonNull(entity.getServer()).getLevel(targetRings.dimension)),
                    new RingsTeleporter(new Vector3d(tPos.x, tPos.y, tPos.z), entity.getYRot(), null));
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
            JSGTransporters.logger.info("{} {}", fluidState.is(TagsRegistry.TRANSPORTER_FLUIDS),
                    fluidState.getFluidType()); // temp
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

    public static void teleportBlocks(Stream<Map.Entry<BlockPos, BlockPos>> poses, RingsAbstractBE sourceRings,
            RingsAbstractBE targetRings, ArrayList<BlockToTeleport> pistonHeads) {
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

    public static void rotateAndSwapVolumes(Volume a, Volume b) {
        Quaternionf rotOffset = a.getRotOffset(b);
        if (!a.getShape().equals(RotationUtil.rotate(b.getShape(), rotOffset))) {
            throw new IllegalArgumentException("Volumes shapes do not match after rotation");
        }
        //TODO implement block rotation on teleport
    }

    public record Volume(Level level, BlockPos backLeftTop, BlockPos frontRightBottom, Direction facing) {

        public Volume {
            if (facing.getAxis() == Axis.Y)
                throw new IllegalArgumentException("Facing direction cannot be vertical");
        }

        Quaternionf getRotOffset(Volume other) {
            final Quaternionf offset = facing.getRotation().difference(other.facing.getRotation());
            return offset;
        }

        BlockPos getShape() {
            return frontRightBottom.subtract(backLeftTop);
        }
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
                            .setValue(PistonHeadBlock.TYPE,
                                    state.getBlock() == Blocks.STICKY_PISTON ? PistonType.STICKY : PistonType.DEFAULT);
                }
                if (pistonHeads != null) {
                    if (level.getBlockState(pos).is(Blocks.PISTON_HEAD)) {
                        pistonHeads.add(this);
                        return;
                    }
                    if (direction != null
                            && level.getBlockState(pos.offset(direction.getNormal())).is(Blocks.PISTON_HEAD)) {
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
                    JSGTransporters.logger.error("Expected block entity at {} in {} but no block entity found", pos,
                            level);
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
