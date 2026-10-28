package dev.tauri.jsgtransporters.common.helpers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import javax.annotation.Nullable;
import java.util.List;

public record RingsTeleporter(Vector3d posFinal, float newYaw, @Nullable List<Entity> passengers) {
    public DimensionTransition createTransition(Entity entity, ServerLevel targetLevel) {
        return new DimensionTransition(targetLevel, new Vec3(posFinal.x(), posFinal.y(), posFinal.z()), new Vec3(0, 0, 0), newYaw, entity.getXRot(), placed -> {
            TeleportHelper.setRotationAndPosition(placed, newYaw, posFinal);
            if (passengers != null) {
                for (Entity passenger : passengers) {
                    passenger.startRiding(placed);
                }
            }
        });
    }
}
