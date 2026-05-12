package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.core.common.item.JSGBlockItem;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsAncientCPBE;
import dev.tauri.jsgtransporters.common.item.controller.RingsAncientControllerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsAncientCPBlock extends AbstractRingsCPBlock {
    public RingsAncientCPBlock() {
        super();
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsAncientCPBE(pPos, pState);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new RingsAncientControllerItem(this);
    }
}
