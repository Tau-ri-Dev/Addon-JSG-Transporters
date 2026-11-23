package dev.tauri.jsgtransporters.common.multistructure.mergehelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import dev.tauri.jsgcore.common.blockstate.JSGProperties;
import dev.tauri.jsgcore.common.multistructure.merging.IMergeHelper;
import dev.tauri.jsgcore.common.util.RotationUtil;
import dev.tauri.jsgtransporters.common.blockentity.lantean_transporter.LanteanTransporterBE;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class LanteanTransporterMergeHelper implements IMergeHelper {

    public final LanteanTransporterBE transporter;
    private Direction horizontalFacing = Direction.SOUTH;
    private boolean merged;

    private static final String[] LAYER_0 = {
        "FFFFF", "FPPPF", "FPPPF", "FXXXF"
    };
    private static final String[] LAYER_1 = {
        "IPPPI", "A___P", "P___P", "F___F"
    };
    private static final String[] LAYER_2 = {
        "IPSPI", "P___P", "P___P", "F___F"
    };
    private static final String[] LAYER_3 = {
        "IPPPI", "P___P", "P___P", "F___F"
    };
    private static final String[] LAYER_4 = {
        "FFFFF", "FPPPF", "FPPPF", "FFFFF"
    };

    public LanteanTransporterMergeHelper(LanteanTransporterBE transporter) {
        this.transporter = transporter;
    }

    @Override
    public boolean checkMergeState() {
        Level level = transporter.getLevel();
        if (level == null) return false;

        for (Map.Entry<BlockPos, BlockState> entry : getBlocks().entrySet()) {
            BlockState actual = level.getBlockState(entry.getKey());
            BlockState expected = entry.getValue();
            if (actual.getBlock() != expected.getBlock()) return false;
            if (expected.hasProperty(JSGProperties.FACING_HORIZONTAL_PROPERTY)
                    && !Objects.equals(actual.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY),
                            expected.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY))) return false;
        }
        return true;
    }

    @Override
    public Map<BlockPos, BlockState> getBlocks() {
        Map<BlockPos, BlockState> blocks = new HashMap<>();
        Level level = transporter.getLevel();
        addLayer(blocks, level, LAYER_0, -1);
        addLayer(blocks, level, LAYER_1, 0);
        addLayer(blocks, level, LAYER_2, 1);
        addLayer(blocks, level, LAYER_3, 2);
        addLayer(blocks, level, LAYER_4, 3);
        return blocks;
    }

    private void addLayer(Map<BlockPos, BlockState> blocks, Level level, String[] layer, int y) {
        for (int z = 0; z < layer.length; z++) {
            String row = layer[z];
            for (int x = 0; x < row.length(); x++) {
                char key = row.charAt(x);
                if (key == '_') continue;

                // Layer 1, row 1, column 0 contains the access panel and is the BE origin.
                BlockPos relative = new BlockPos(x, y, z - 1);
                BlockPos                 worldPos = relative(relative).offset(transporter.getBlockPos());
                blocks.put(worldPos, getBlockState(key, level, worldPos, relative));
            }
        }
    }

    private BlockPos relative(BlockPos relative) {
        return RotationUtil.rotate(relative, RotationUtil.getRotation(Direction.SOUTH, horizontalFacing));
    }

    private BlockState getBlockState(char key, Level level, BlockPos worldPos, BlockPos relativePos) {
        Block block = switch (key) {
            case 'F' -> JSGTBlocks.LANTEAN_TRANSPORTER_FRAME.get();
            case 'P' -> JSGTBlocks.LANTEAN_TRANSPORTER_PLATFORM.get();
            case 'A' -> JSGTBlocks.LANTEAN_TRANSPORTER_ACCESS_PANEL.get();
            case 'S' -> JSGTBlocks.LANTEAN_TRANSPORTER_SCREEN.get();
            case 'X' -> JSGTBlocks.LANTEAN_TRANSPORTER_PANELING.get();
            case 'I' -> JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get();
            default -> throw new IllegalArgumentException("Unknown transporter multistructure key: " + key);
        };

        BlockState state = block.defaultBlockState();
        if (key == 'A') {
            state = setHorizontalFacing(state, new BlockPos(1, 0, 0));
        } else if (key == 'S') {
            BlockPos normal = relativePos.getX() == 1
                    ? new BlockPos(1, 0, 0)
                    : new BlockPos(-1, 0, 0);
            state = setHorizontalFacing(state, normal);
        }
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            boolean waterlogged = level != null
                    && level.getFluidState(worldPos).getType() == Fluids.WATER;
            state = state.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
        }
        return state;
    }

    private BlockState setHorizontalFacing(BlockState state, BlockPos relativeNormal) {
        if (!state.hasProperty(JSGProperties.FACING_HORIZONTAL_PROPERTY)) return state;

        BlockPos rotatedNormal = relative(relativeNormal);
        int dx = rotatedNormal.getX();
        int dz = rotatedNormal.getZ();
        Direction facing;
        if (Math.abs(dx) >= Math.abs(dz)) {
            facing = dx >= 0 ? Direction.EAST : Direction.WEST;
        } else {
            facing = dz >= 0 ? Direction.SOUTH : Direction.NORTH;
        }
        return state.setValue(JSGProperties.FACING_HORIZONTAL_PROPERTY, facing);
    }

    @Override
    public boolean shouldBeMerged() {
        return merged;
    }

    @Override
    public void tick(@NotNull Level level) {
        BlockState state = transporter.getBlockState();
        if (state.hasProperty(JSGProperties.FACING_HORIZONTAL_PROPERTY)) {
            horizontalFacing = state.getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        }
        if (!level.isClientSide && level.getGameTime() % 20 == 0) {
            updateMemberStateAndCheck(null);
        }
    }

    @Override
    public List<MemberAutoBuildBlock> getAbsentBlockPositions(Level level, boolean chevron) {
        List<MemberAutoBuildBlock> result = new ArrayList<>();
        for (Map.Entry<BlockPos, BlockState> entry : getBlocks().entrySet()) {
            BlockState actual = level.getBlockState(entry.getKey());
            if (actual.isAir() || actual.canBeReplaced()) {
                result.add(new MemberAutoBuildBlock(entry.getKey(), entry.getValue()));
            }
        }
        return result;
    }

    @Override
    public Direction getHorizontalFacing() {
        return horizontalFacing;
    }

    @Override
    public BlockPos getTopBlock() {
        return transporter.getBlockPos().above(3);
    }

    @Override
    public BlockPos getTopBlockAboveBase() {
        return transporter.getBlockPos().above(3);
    }

    @Override
    public Direction getVerticalFacing() {
        return null;
    }

    @Override
    public void updateMemberStateAndCheck(Boolean force) {
        boolean shouldBeMerged = force != null ? force : checkMergeState();
        if (shouldBeMerged != merged) {
            merged = shouldBeMerged;
            transporter.setChanged();
        }
    }
}
