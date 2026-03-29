package dev.tauri.jsgtransporters.common.helpers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.joml.Vector3d;

import java.util.function.Function;

public record RingsTeleporter(Vector3d posFinal, float newYaw) implements ITeleporter {

    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(
                new Vec3(posFinal.x(), posFinal.y(), posFinal.z()),
                Vec3.ZERO,
                newYaw,
                entity.getXRot()
        );
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity placedEntity = repositionEntity.apply(false);
        if (placedEntity == null) return null;

        TeleportHelper.setRotationAndPosition(placedEntity, newYaw, posFinal);
        return placedEntity;
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