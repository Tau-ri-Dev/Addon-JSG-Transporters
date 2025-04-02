package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.block.controller.RingsGoauldCPBlock;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class OriCPRaycaster extends Raycaster {

    public static final OriCPRaycaster INSTANCE = new OriCPRaycaster();
    public static final ArrayList<RayCastedButton> BUTTONS = new ArrayList<>() {{

    }};

    public static void register() {
        Raycaster.register(RingsOriCPBlock.class, INSTANCE);
    }

    @Override
    protected List<RayCastedButton> getButtons() {
        return BUTTONS;
    }

    @Override
    protected Vector3f getTranslation(Level level, BlockPos blockPos) {
        var facing = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        return getTranslation(facing);
    }


    protected static final Vector3f NORTH_TRANSLATION = new Vector3f(0, 0, 0);
    protected static final Vector3f EAST_TRANSLATION = new Vector3f(1, 0, 0);
    protected static final Vector3f SOUTH_TRANSLATION = new Vector3f(1, 0, 1);
    protected static final Vector3f WEST_TRANSLATION = new Vector3f(0, 0, 1);

    public static Vector3f getTranslation(Direction facing) {
        return switch (facing) {
            case EAST -> EAST_TRANSLATION;
            case SOUTH -> SOUTH_TRANSLATION;
            case WEST -> WEST_TRANSLATION;
            default -> NORTH_TRANSLATION;
        };
    }

    @Override
    protected boolean buttonClicked(Level level, Player player, int buttonId, BlockPos blockPos, InteractionHand interactionHand) {
        return false;
    }

    @Override
    public boolean onActivated(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand) {
        if (interactionHand != InteractionHand.MAIN_HAND) return false;
        var direction = level.getBlockState(blockPos).getValue(JSGProperties.FACING_HORIZONTAL_PROPERTY);
        var rotation = GoauldCPRaycaster.getIntRotation(direction);
        return super.onActivated(level, blockPos, player, rotation, interactionHand);
    }
}
