package dev.tauri.jsgtransporters.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tauri.jsg.stargate.BiomeOverlayEnum;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ModelsHolder {
    RING_GOAULD("rings/rings_goauld.obj", "rings/rings_goauld.jpg", false),
    RING_ANCIENT("rings/rings_ancient.obj", "rings/rings_ancient.jpg", false),
    RING_ORI("rings/rings_ori.obj", "rings/rings_ori.jpg", false),

    RINGS_CONTROLLER_GOAULD("rings/controller/goauld/plate_goauld.obj", "rings/controller/goauld/goauld_panel.jpg", true),
    RINGS_CONTROLLER_GOAULD_LIGHT("rings/controller/goauld/indicator_lights.obj", "rings/controller/goauld/goauld_light.jpg", true),
    RINGS_CONTROLLER_ORI_BASE("rings/controller/ori/base.obj", "rings/controller/ori/base.png", false),
    RINGS_CONTROLLER_ANCIENT_BASE("rings/controller/ancient/body_ancient.obj", "rings/controller/ancient/base.png", false),
    ;
    public final ResourceLocation model;
    public final Map<BiomeOverlayEnum, ResourceLocation> biomeTextureResourceMap = new HashMap<>();
    private final List<BiomeOverlayEnum> nonExistingReported = new ArrayList<>();

    ModelsHolder(String modelPath, String texturePath, boolean byOverlay) {
        this.model = Constants.LOADERS_HOLDER.model().getModelResource(modelPath);

        for (BiomeOverlayEnum biomeOverlay : BiomeOverlayEnum.values()) {
            if (!byOverlay) {
                biomeTextureResourceMap.put(biomeOverlay, Constants.LOADERS_HOLDER.texture().getTextureResource(texturePath));
            } else {
                String[] split = texturePath.split("\\.");
                biomeTextureResourceMap.put(biomeOverlay, Constants.LOADERS_HOLDER.texture().getTextureResource(split[0] + biomeOverlay.getSuffix() + "." + split[1]));
            }
        }
    }

    public void render(PoseStack ps) {
        Constants.LOADERS_HOLDER.model().getModel(model).render(ps);
    }

    public void render(PoseStack ps, boolean renderEmissive) {
        Constants.LOADERS_HOLDER.model().getModel(model).render(ps, renderEmissive);
    }

    public void bindTexture(BiomeOverlayEnum biomeOverlay) {
        ResourceLocation resourceLocation = biomeTextureResourceMap.get(biomeOverlay);
        bindTexture(biomeOverlay, resourceLocation);
    }

    private void bindTexture(BiomeOverlayEnum biomeOverlay, ResourceLocation resourceLocation) {
        if (!Constants.LOADERS_HOLDER.texture().isTextureLoaded(resourceLocation)) {
            if (!nonExistingReported.contains(biomeOverlay)) {
                JSGTransporters.logger.error("{} tried to use BiomeOverlay {} but it doesn't exist. ({})", this, biomeOverlay, resourceLocation);
                nonExistingReported.add(biomeOverlay);
            }
            resourceLocation = biomeTextureResourceMap.get(BiomeOverlayEnum.NORMAL);
        }

        Constants.LOADERS_HOLDER.texture().getTexture(resourceLocation).bindTexture();
    }

    public void bindTextureAndRender(PoseStack ps) {
        bindTextureAndRender(BiomeOverlayEnum.NORMAL, ps);
    }

    public void bindTextureAndRender(BiomeOverlayEnum biomeOverlay, PoseStack ps) {
        bindTexture(biomeOverlay);
        render(ps);
    }
}
