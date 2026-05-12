package dev.tauri.jsgtransporters.common.block.controller;

import dev.tauri.jsg.core.common.item.JSGBlockItem;
import dev.tauri.jsgtransporters.common.blockentity.controller.RingsGoauldCPBE;
import dev.tauri.jsgtransporters.common.item.controller.RingsGoauldControllerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingsGoauldCPBlock extends AbstractRingsCPBlock {
    public RingsGoauldCPBlock() {
        super();
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RingsGoauldCPBE(pPos, pState);
    }

    @Override
    public JSGBlockItem getItemBlock() {
        return new RingsGoauldControllerItem(this);
    }
}
