package dev.tauri.jsgtransporters.common.raycaster;

import dev.tauri.jsg.property.JSGProperties;
import dev.tauri.jsg.raycaster.Raycaster;
import dev.tauri.jsg.raycaster.util.RayCastedButton;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.block.controller.RingsOriCPBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class OriCPRaycaster extends Raycaster {

    public static final OriCPRaycaster INSTANCE = new OriCPRaycaster();
    public static final List<RayCastedButton> BUTTONS = List.of();

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


    protected static final Vector3f NORTH_TRANSLATION = new Vector3f(0.5f, 0.5f, 0.97f);
    protected static final Vector3f EAST_TRANSLATION = new Vector3f(0.03f, 0.5f, 0.49f);
    protected static final Vector3f SOUTH_TRANSLATION = new Vector3f(0.5f, 0.5f, 0.03f);
    protected static final Vector3f WEST_TRANSLATION = new Vector3f(0.97f, 0.5f, 0.51f);

    public static Vector3f getTranslation(Direction facing) {
        return switch (facing) {
            case EAST -> EAST_TRANSLATION;
            case SOUTH -> SOUTH_TRANSLATION;
            case WEST -> WEST_TRANSLATION;
            default -> NORTH_TRANSLATION;
        };
    }

    public static int getIntRotation(Direction direction) {
        return switch (direction) {
            case EAST -> 90;
            case NORTH -> 180;
            case WEST -> 270;
            default -> 0;
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
        var rotation = getIntRotation(direction);
        return super.onActivated(level, blockPos, player, rotation, interactionHand);
    }

    @Override
    public boolean doesRayIntersectPolygon(Vec3 eye, Vec3 direction, List<Vec3> polygon) {
        var copy = new ArrayList<Vec3>();
        polygon.forEach(v -> copy.add(v.multiply(1.5f, 1.5f, 1.5f)));
        return super.doesRayIntersectPolygon(eye, direction, copy);
    }
}
