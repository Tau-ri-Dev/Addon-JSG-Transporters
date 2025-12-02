package dev.tauri.jsgtransporters.common.helpers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.joml.Vector3d;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public record RingsTeleporter(Vector3d posFinal, float newYaw, @Nullable List<Entity> passengers) implements ITeleporter {

    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(new Vec3(posFinal.x(), posFinal.y(), posFinal.z()), new Vec3(0, 0, 0), newYaw, entity.getXRot());
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity e = repositionEntity.apply(false);
        if (e == null) return null;
        TeleportHelper.setRotationAndPosition(e, newYaw, posFinal);
        if (passengers != null) {
            for (Entity passenger : passengers) {
                passenger.startRiding(e);
            }
        }
        return e;
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }

    @Override
    public boolean isVanilla() {
        return false;
    }
}
