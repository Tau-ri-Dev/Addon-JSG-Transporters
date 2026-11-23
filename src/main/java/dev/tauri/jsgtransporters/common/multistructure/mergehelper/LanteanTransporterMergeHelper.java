package dev.tauri.jsgtransporters.common.multistructure.mergehelper;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

import dev.tauri.jsgcore.common.multistructure.merging.IMergeHelper;
import dev.tauri.jsgtransporters.common.blockentity.lantean_transporter.LanteanTransporterBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LanteanTransporterMergeHelper implements IMergeHelper {

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
        return null;
    }

    @Override
    public void updateMemberStateAndCheck(Boolean arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemberStateAndCheck'");
    }

}
