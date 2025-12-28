package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.api.blockstates.JSGProperties;
import dev.tauri.jsg.api.raycaster.Raycaster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractCPRaycaster extends Raycaster {
    public static int getIntRotation(Direction direction) {
        return switch (direction) {
            case EAST -> 90;
            case NORTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    @Override
    public float getRotation(Level level, BlockPos blockPos, Player player) {
        var direction = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        return getIntRotation(direction);
    }

    @Override
    public boolean onActivated(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand) {
        if (interactionHand != InteractionHand.MAIN_HAND) return false;
        return super.onActivated(level, blockPos, player, interactionHand);
    }
}
