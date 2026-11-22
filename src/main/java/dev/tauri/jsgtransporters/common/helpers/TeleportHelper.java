package dev.tauri.jsgtransporters.common.helpers;

import dev.tauri.jsgcore.common.registry.helper.FluidHelper;
import dev.tauri.jsgcore.common.util.JSGAxisAlignedBB;
import dev.tauri.jsgcore.common.util.RotationUtil;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import dev.tauri.jsgtransporters.common.config.JSGTConfig;
import dev.tauri.jsgtransporters.common.helpers.TeleportHelper.BlockToTeleport;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTFluidTags;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import org.joml.Quaternionf;
import org.joml.Vector3d;

import com.google.common.base.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        setRotationAndPosition(entity, yawRotated, yawRotated, pos);
    }

    public static void setRotationAndPosition(Entity entity, float headYaw, float bodyYaw, Vector3d pos) {
        if (entity instanceof ServerPlayer player) {
            player.connection.teleport(pos.x, pos.y, pos.z, bodyYaw, entity.getXRot());
        } else {
            entity.teleportTo(pos.x, pos.y, pos.z);
        }
        entity.setYRot(bodyYaw);
        entity.setYHeadRot(headYaw);
    }

    private static float rotateYaw(float yaw, Quaternionf rotOffset) {
        float rotationDegrees = (float) Math.toDegrees(2.0 * Math.atan2(rotOffset.y(), rotOffset.w()));
        return Mth.wrapDegrees(yaw - rotationDegrees);
    }

    private static Rotation toBlockRotation(Quaternionf rotOffset) {
        float rotationDegrees = Mth.wrapDegrees(
                (float) Math.toDegrees(2.0 * Math.atan2(rotOffset.y(), rotOffset.w())));
        int quarterTurns = Math.round(rotationDegrees / 90.0f);
        if (Math.abs(rotationDegrees - quarterTurns * 90.0f) > 0.001f)
            throw new IllegalArgumentException("Block rotation must be a multiple of 90 degrees: " + rotationDegrees);

        return switch (quarterTurns) {
            case -2, 2 -> Rotation.CLOCKWISE_180;
            case -1 -> Rotation.CLOCKWISE_90;
            case 0 -> Rotation.NONE;
            case 1 -> Rotation.COUNTERCLOCKWISE_90;
            default -> throw new IllegalArgumentException("Unsupported block rotation: " + rotationDegrees);
        };
    }

    @Nonnull
    public static BlockState applyStateChanges(Level level, BlockPos pos, BlockState oldState) {
        return applyStateChanges(level, pos, oldState, new Quaternionf());
    }

    @Nonnull
    public static BlockState applyStateChanges(Level level, BlockPos pos, BlockState oldState, Quaternionf rotOffset) {
        final FluidState waterState = Blocks.WATER.defaultBlockState().getFluidState();
        FluidState fluidState = oldState.getFluidState();
        if (oldState.getBlock() instanceof LiquidBlock && fluidState.isSource()) {
            // temp
            JSGTransporters.logger.info("{} {}", fluidState.is(JSGTFluidTags.PERMANENT_TRANSPORT_FLUIDS), fluidState.getFluidType()); // temp
            Fluid fluid = fluidState.getType();
            if (fluid instanceof FlowingFluid flowFluid && switch (JSGTConfig.General.ringsFluidTreatmentMode.get()) {
                case Always -> true;
                case Never -> false;
                case ByTag -> fluidState.is(JSGTFluidTags.PERMANENT_TRANSPORT_FLUIDS);
                case ExcludeTag -> !fluidState.is(JSGTFluidTags.PERMANENT_TRANSPORT_FLUIDS);
            }) {
                FluidState newState = flowFluid.getFlowing().defaultFluidState().setValue(FlowingFluid.LEVEL, 7);
                return newState.createLegacyBlock();
            }
        } else if (oldState.hasProperty(BlockStateProperties.WATERLOGGED) &&
                oldState.getValue(BlockStateProperties.WATERLOGGED) &&
                switch (JSGTConfig.General.ringsFluidTreatmentMode.get()) {
                    case Always -> true;
                    case Never -> false;
                    case ByTag -> waterState.is(JSGTFluidTags.PERMANENT_TRANSPORT_FLUIDS);
                    case ExcludeTag -> !waterState.is(JSGTFluidTags.PERMANENT_TRANSPORT_FLUIDS);
                }) {
            oldState = oldState.setValue(BlockStateProperties.WATERLOGGED, false);
        }
        if (oldState.hasProperty(BlockStateProperties.EXTENDED) && oldState.getValue(BlockStateProperties.EXTENDED)) {
            oldState = oldState.setValue(BlockStateProperties.EXTENDED, false);
        }
        
        return oldState.rotate(level, pos, toBlockRotation(rotOffset));
    }

    public interface TransportPowerConsumer {
        /**
         * Consumes the ammount of power required to transport the blocks/entities, if insufficient power is present should throw IllegalStateException
         * 
         * @param energySumLocal the sum of the energy of all blocks and entities being teleported from the local volume
         * @param energySumRemote the sum of the energy of all blocks and entities being teleported from the remote volume
         * @throws IllegalStateException if insufficient power is present
         */
        void consumeTransportPower(long energySumLocal, long energySumRemote) throws IllegalStateException;
    }

    public static boolean rotateAndSwapVolumes(@Nonnull Volume a, @Nonnull Volume b, Function<Object, Long> energyConsumptionLocal, Function<Object, Long> energyConsumptionRemote, TransportPowerConsumer powerConsumer) {
        Quaternionf abRotOffset = a.getRotOffset(b);
        Quaternionf baRotOffset = b.getRotOffset(a);
        if (!a.getShapeSize().equals(b.getRotatedShapeSize(abRotOffset))) {
            throw new IllegalArgumentException("Volumes shapes do not match after rotation A: %s, B: %s, Rotated B by %s: %s".formatted(a.getShapeSize(), b.getShapeSize(), abRotOffset, b.getRotatedShapeSize(abRotOffset)));
        }
        Stream<BlockPos> poses = StreamSupport.stream(BlockPos.betweenClosed(new BlockPos(0, 0, 0), a.getShape()).spliterator(), false);

        Stream<Map.Entry<BlockPos, BlockPos>> posesToTeleport = poses.map(BlockPos::immutable)
                .map(p -> Map.entry(a.frontRightBottom().offset(p), b.frontRightBottom().offset(RotationUtil.rotate(p, abRotOffset))))
                .peek(pp -> {
                    BlockPos sourcePos = pp.getKey();
                    BlockPos targetPos = pp.getValue();
                    if (a.level().getBlockState(sourcePos).is(JSGTBlockTags.UNTRANSPORTABLE_BLOCK)
                            || b.level().getBlockState(targetPos).is(JSGTBlockTags.UNTRANSPORTABLE_BLOCK)) {
                        throw new UntransportableBlockException(
                                "Cannot teleport untransportable block between %s and %s"
                                        .formatted(sourcePos, targetPos));
                    }
                });
        var blocksResult = setupTeleportBlocks(posesToTeleport, a.level(), b.level(), abRotOffset, baRotOffset, energyConsumptionLocal, energyConsumptionRemote);
        var entitiesResult = rotateAndSwapVolumeEntities(a, b, abRotOffset, baRotOffset, energyConsumptionLocal, energyConsumptionRemote);
        var blocksCost = blocksResult.getKey();
        var entitiesCost = entitiesResult.getKey();
        try{
            powerConsumer.consumeTransportPower(blocksCost.getKey() + entitiesCost.getKey(), blocksCost.getValue() + entitiesCost.getValue());}
        catch (IllegalStateException e) {
            JSGTransporters.logger.info("Insufficient power to transport between volumes %s and %s, required: %sRF local, %sRF remote".formatted(a, b, blocksCost.getKey() + entitiesCost.getKey(), blocksCost.getValue() + entitiesCost.getValue()));
            return false;
        }
        blocksResult.getValue().run();
        entitiesResult.getValue().run();
        return true;
    }

    private static Map.Entry<Map.Entry<Long, Long>, Runnable> setupTeleportBlocks(@Nonnull Stream<Map.Entry<BlockPos, BlockPos>> poses,@Nonnull Level localLevel,@Nonnull  Level remoteLevel, @Nonnull Quaternionf abRotOffset, @Nonnull Quaternionf baRotOffset, Function<Object, Long> energyConsunptionLocal, Function<Object, Long> energyConsunptionRemote) {
        ArrayList<BlockToTeleport> pistonHeads = new ArrayList<>();
        long[] coeffSum = {0L, 0L};
        var toPlace = poses.map(pp -> {
            var local = pp.getKey();
            var remote = pp.getValue();
            var localBlock = TeleportHelper.applyStateChanges(localLevel, local, localLevel.getBlockState(local), abRotOffset);
            var remoteBlock = TeleportHelper.applyStateChanges(remoteLevel, remote, remoteLevel.getBlockState(remote), baRotOffset);

            // map blocks
            var bttLocal = Optional.ofNullable(localLevel.getBlockEntity(local))
                    .map(net.minecraft.world.level.block.entity.BlockEntity::serializeNBT)
                    .<BlockToTeleport>map(nbt -> new BlockToTeleport.BlockEntity(localBlock, nbt, remote, remoteLevel, local, localLevel))
                    .orElseGet(() -> {
                        if (localBlock.getBlock() == Blocks.PISTON_HEAD)
                            return new BlockToTeleport.Void(local, localLevel);
                        return new BlockToTeleport.Block(localBlock, remote, remoteLevel, local, localLevel);
                    });
            var bttRemote = Optional.ofNullable(remoteLevel.getBlockEntity(remote))
                    .map(net.minecraft.world.level.block.entity.BlockEntity::serializeNBT)
                    .<BlockToTeleport>map(nbt -> new BlockToTeleport.BlockEntity(remoteBlock, nbt, local, localLevel, remote, remoteLevel))
                    .orElseGet(() -> {
                        if (remoteBlock.getBlock() == Blocks.PISTON_HEAD)
                            return new BlockToTeleport.Void(remote, remoteLevel);
                        return new BlockToTeleport.Block(remoteBlock, local, localLevel, remote, remoteLevel);
                    });
            coeffSum[0] += energyConsunptionLocal.apply(bttLocal);
            coeffSum[1] += energyConsunptionRemote.apply(bttRemote);
            
            return Map.entry(bttLocal, bttRemote);
        }).toList().stream();
        pistonHeads.forEach(p -> p.placeOrAdd(null));
        return Map.entry(Map.entry(coeffSum[0], coeffSum[1]), () -> {toPlace.peek(pp -> {
            var local = pp.getKey();
            var remote = pp.getValue();
            local.removeLocal();
            remote.removeLocal();
            local.placeOrAdd(pistonHeads);
            remote.placeOrAdd(pistonHeads);
        }).forEach(pp -> {
            pp.getKey().placeOrAdd(null);
            pp.getValue().placeOrAdd(null);
        });});
    }

    private static Map.Entry<Map.Entry<Long, Long>, Runnable> rotateAndSwapVolumeEntities(Volume a, Volume b, Quaternionf abRotOffset, Quaternionf baRotOffset, Function<Object, Long> energyConsumptionLocal, Function<Object, Long> energyConsumptionRemote) {
        var entitiesInA = a.level().getEntities(null, new JSGAxisAlignedBB(a.backLeftTop().getCenter(), a.frontRightBottom().getCenter()).grow(0.5, 0.5, 0.5));
        var entitiesInB = b.level().getEntities(null, new JSGAxisAlignedBB(b.backLeftTop().getCenter(), b.frontRightBottom().getCenter()).grow(0.5, 0.5, 0.5));
        long energySumLocal = 0L;
        long energySumRemote = 0L;
        for (var e : entitiesInA) {
            energySumLocal += energyConsumptionLocal.apply(e);
        }
        for (var e : entitiesInB) {
            energySumRemote += energyConsumptionRemote.apply(e);
        }
        return Map.entry(Map.entry(energySumLocal, energySumRemote), () -> {
            for (var e : entitiesInA) {
                TeleportHelper.teleportEntityByVolume(e, a, b, abRotOffset);
            }
            for (var e : entitiesInB) {
                TeleportHelper.teleportEntityByVolume(e, b, a, baRotOffset);
            }
        });
    }

    public static void teleportEntityByVolume(Entity entity, Volume sourceVolume, Volume targetVolume, Quaternionf rotOffset) {
        ResourceKey<Level> sourceDim = sourceVolume.level().dimension();

        var offset = entity.position()
                .subtract(sourceVolume.frontRightBottom().getCenter());
        var tPos = targetVolume.frontRightBottom().getCenter()
                .add(RotationUtil.rotate(offset, rotOffset));

        if (sourceDim == targetVolume.level().dimension()) {
            setRotationAndPosition(entity, rotateYaw(entity.getYHeadRot(), rotOffset), rotateYaw(entity.getYRot(), rotOffset), new Vector3d(tPos.x, tPos.y, tPos.z));
        } else {
            // if (!fireTravelToDimEvent(entity, targetRings.dimension))
            // return;

            entity.changeDimension(
                    Objects.requireNonNull(Objects.requireNonNull(entity.getServer()).getLevel(targetVolume.level().dimension())),
                    new RingsTeleporter(new Vector3d(tPos.x, tPos.y, tPos.z), rotateYaw(entity.getYRot(), rotOffset), null));
        }
    }

    public record Volume(@Nonnull Level level, @Nonnull BlockPos backLeftTop, @Nonnull BlockPos frontRightBottom,@Nonnull Direction facing) {

        public Volume {
            if (facing.getAxis() == Axis.Y)
                throw new IllegalArgumentException("Facing direction cannot be vertical");
            if (backLeftTop.getY() < frontRightBottom.getY())
                throw new IllegalArgumentException("Back-left-top must be at or above front-right-bottom");

            boolean backIsLeft = switch (facing) {
                case NORTH -> backLeftTop.getX() <= frontRightBottom.getX();
                case EAST -> backLeftTop.getZ() <= frontRightBottom.getZ();
                case SOUTH -> backLeftTop.getX() >= frontRightBottom.getX();
                case WEST -> backLeftTop.getZ() >= frontRightBottom.getZ();
                default -> false;
            };
            boolean frontIsForward = switch (facing) {
                case NORTH -> frontRightBottom.getZ() < backLeftTop.getZ();
                case EAST -> frontRightBottom.getX() > backLeftTop.getX();
                case SOUTH -> frontRightBottom.getZ() > backLeftTop.getZ();
                case WEST -> frontRightBottom.getX() < backLeftTop.getX();
                default -> false;
            };
            if (!backIsLeft || !frontIsForward)
                throw new IllegalArgumentException(
                        "Volume corners do not match facing direction: back-left-top must be behind and left of front-right-bottom");
        }

        @SuppressWarnings("null")
        @Nonnull 
        Quaternionf getRotOffset(Volume other) {
            return new Quaternionf().rotateY(Mth.wrapDegrees(facing().toYRot()- other.facing().toYRot())*Mth.DEG_TO_RAD);
        }

        @SuppressWarnings("null")
        @Nonnull
        BlockPos getShape() {
            return backLeftTop.subtract(frontRightBottom);
        }

        @Nonnull 
        BlockPos getShapeSize() {
            final BlockPos size = new BlockPos(Math.abs(getShape().getX()), Math.abs(getShape().getY()), Math.abs(getShape().getZ()));
            return size;
        }
        @Nonnull
        BlockPos getRotatedShapeSize(Quaternionf rotOffset) {
            return new BlockPos(Math.abs(RotationUtil.rotate(getShape(), rotOffset).getX()), Math.abs(RotationUtil.rotate(getShape(), rotOffset).getY()), Math.abs(RotationUtil.rotate(getShape(), rotOffset).getZ()));
        }
    }

    public sealed interface BlockToTeleport {
        int PLACE_FLAGS = 2 | 32 | 16 | 64;

        void removeLocal();

        void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads);

        double getEnergyCoefficient();

        record Block(BlockState state, BlockPos pos, Level level, BlockPos oldPos, Level oldLevel) implements BlockToTeleport {

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

            @Override
            public void removeLocal() {
                oldLevel.setBlock(oldPos, Blocks.AIR.defaultBlockState(), BlockToTeleport.PLACE_FLAGS);
            }
        }

        record Void(BlockPos oldPos, Level oldLevel) implements BlockToTeleport {

            @Override
            public void placeOrAdd(ArrayList<BlockToTeleport> pistonHeads) {}

            @Override
            public double getEnergyCoefficient() {
                return 0;
            }

            @Override
            public void removeLocal() {}
        }

        record BlockEntity(BlockState state, CompoundTag nbt, BlockPos pos, Level level, BlockPos oldPos, Level oldLevel) implements BlockToTeleport {
            @Override
            public void removeLocal() {
                var be = oldLevel.getBlockEntity(oldPos);
                if (be instanceof Container container) {
                    container.clearContent();
                } else if (be != null) {
                    be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((itemHandler) -> {
                        for (var slot = 0; slot < itemHandler.getSlots(); slot++) {
                            itemHandler.getStackInSlot(slot).setCount(0);
                        }
                    });
                }
                oldLevel.setBlock(oldPos, Blocks.AIR.defaultBlockState(), BlockToTeleport.PLACE_FLAGS);
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
