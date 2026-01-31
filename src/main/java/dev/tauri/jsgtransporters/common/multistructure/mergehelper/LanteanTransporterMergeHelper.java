package dev.tauri.jsgtransporters.common.multistructure.mergehelper;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

import dev.tauri.jsg.api.multistructure.merging.IMergeHelper;
import dev.tauri.jsgtransporters.common.blockentity.lantean_transporter.LanteanTransporterBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LanteanTransporterMergeHelper implements IMergeHelper {
    private static final int REAR_PYLON_OFFSET_X = 1;
    private static final int REAR_PYLON_OFFSET_Z = 1;
    private static final int FRONT_PYLON_OFFSET_X = 2;
    private static final int FRONT_PYLON_OFFSET_Z = 2;
    public static final List<BlockPos> PYLON_POSITIONS = List.of(
            new BlockPos(REAR_PYLON_OFFSET_X, 0, REAR_PYLON_OFFSET_Z),
            new BlockPos(FRONT_PYLON_OFFSET_X, 0, FRONT_PYLON_OFFSET_Z),
            new BlockPos(-REAR_PYLON_OFFSET_X, 0, REAR_PYLON_OFFSET_Z),
            new BlockPos(-FRONT_PYLON_OFFSET_X, 0, FRONT_PYLON_OFFSET_Z),
            new BlockPos(REAR_PYLON_OFFSET_X, 1, REAR_PYLON_OFFSET_Z),
            new BlockPos(FRONT_PYLON_OFFSET_X, 1, FRONT_PYLON_OFFSET_Z),
            new BlockPos(-REAR_PYLON_OFFSET_X, 1, REAR_PYLON_OFFSET_Z),
            new BlockPos(-FRONT_PYLON_OFFSET_X, 1, FRONT_PYLON_OFFSET_Z),
            new BlockPos(REAR_PYLON_OFFSET_X, 2, REAR_PYLON_OFFSET_Z),
            new BlockPos(FRONT_PYLON_OFFSET_X, 2, FRONT_PYLON_OFFSET_Z),
            new BlockPos(-REAR_PYLON_OFFSET_X, 2, REAR_PYLON_OFFSET_Z),
            new BlockPos(-FRONT_PYLON_OFFSET_X, 2, FRONT_PYLON_OFFSET_Z));
    public static final List<BlockPos> PLATFORM_POSITIONS = IntStream
            .rangeClosed(REAR_PYLON_OFFSET_X, -REAR_PYLON_OFFSET_X).boxed().flatMap(
                    x -> IntStream.rangeClosed(REAR_PYLON_OFFSET_Z, FRONT_PYLON_OFFSET_Z).mapToObj(
                            z -> new BlockPos(x, 0, z)))
            .toList();

    public final LanteanTransporterBE transporter;

    public LanteanTransporterMergeHelper(LanteanTransporterBE transporter) {
        this.transporter = transporter;
    }

    @Override
    public boolean checkMergeState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkMergeState'");
    }

    @Override
    public Map<BlockPos, BlockState> getBlocks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlocks'");
    }

    @Override
    public boolean shouldBeMerged() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shouldBeMerged'");
    }

    @Override
    public void tick(@NotNull Level arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
    }

    @Override
    public List<MemberAutoBuildBlock> getAbsentBlockPositions(Level arg0, boolean arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAbsentBlockPositions'");
    }

    @Override
    public Direction getHorizontalFacing() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHorizontalFacing'");
    }

    @Override
    public BlockPos getTopBlock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopBlock'");
    }

    @Override
    public BlockPos getTopBlockAboveBase() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopBlockAboveBase'");
    }

    @Override
    public Direction getVerticalFacing() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVerticalFacing'");
    }

    @Override
    public void updateMemberStateAndCheck(Boolean arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemberStateAndCheck'");
    }

}
