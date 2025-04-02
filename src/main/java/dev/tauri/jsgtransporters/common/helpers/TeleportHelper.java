package dev.tauri.jsgtransporters.common.helpers;

import dev.tauri.jsg.stargate.teleportation.JSGGateTeleporter;
import dev.tauri.jsg.util.vectors.Vector3f;
import dev.tauri.jsgtransporters.common.rings.network.RingsPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.Objects;

import static dev.tauri.jsg.stargate.teleportation.TeleportHelper.fireTravelToDimEvent;
import static dev.tauri.jsg.stargate.teleportation.TeleportHelper.setRotationAndPositionAndMotion;

public class TeleportHelper {
    public static void teleportEntity(Entity entity, RingsPos sourceRings, RingsPos targetRings) {
        Level world = entity.level();
        ResourceKey<Level> sourceDim = world.dimension();

        var offset = entity.position().subtract(sourceRings.ringsPos.getCenter().add(0, sourceRings.getBlockEntity().getVerticalOffset(), 0));
        var tPos = targetRings.ringsPos.getCenter().add(0, targetRings.getBlockEntity().getVerticalOffset(), 0).add(offset);

        if (sourceDim == targetRings.dimension) {
            setRotationAndPositionAndMotion(entity, entity.getYHeadRot(), new Vector3d(tPos.x, tPos.y, tPos.z), new Vector3f(), false);
        } else {
            if (!fireTravelToDimEvent(entity, targetRings.dimension)) return;

            entity.changeDimension(Objects.requireNonNull(Objects.requireNonNull(entity.getServer()).getLevel(targetRings.dimension)), new JSGGateTeleporter(new Vector3d(tPos.x, tPos.y, tPos.z), new Vector3f(), entity.getYRot(), null, false, null));
        }
    }
}
