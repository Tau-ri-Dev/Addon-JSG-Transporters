package dev.tauri.jsgtransporters.client;

import dev.tauri.jsg.core.client.IModelsHolder;
import dev.tauri.jsg.core.client.LoadersHolder;
import dev.tauri.jsg.core.common.entity.BiomeOverlayInstance;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ModelsHolder implements IModelsHolder {
    RING_GOAULD("rings/rings_goauld.obj", "rings/rings_goauld.jpg", true),
    RING_ANCIENT("rings/rings_ancient.obj", "rings/rings_ancient.jpg", true),
    RING_ORI("rings/rings_ori.obj", "rings/rings_ori.jpg", true),

    RINGS_CONTROLLER_GOAULD("rings/controller/goauld/plate_goauld.obj", "rings/controller/goauld/goauld_panel.jpg", true),
    RINGS_CONTROLLER_GOAULD_LIGHT("rings/controller/goauld/indicator_lights.obj", "rings/controller/goauld/goauld_light.jpg", true),
    RINGS_CONTROLLER_ORI_BASE("rings/controller/ori/base.obj", "rings/controller/ori/base.png", true),
    RINGS_CONTROLLER_ANCIENT_BASE("rings/controller/ancient/body_ancient.obj", "rings/controller/ancient/base.png", true),
    ;
    public final ResourceLocation model;
    public final Map<BiomeOverlayInstance, ResourceLocation> biomeTextureResourceMap = new HashMap<>();
    private final List<BiomeOverlayInstance> nonExistingReported = new ArrayList<>();

    ModelsHolder(String modelPath, String texturePath, boolean byOverlay) {
        this.model = ClientConstants.LOADERS_HOLDER.model().getModelResource(modelPath);
        loadEntry(texturePath, byOverlay);
    }

    @Override
    public @Nonnull LoadersHolder getLoadersHolder() {
        return ClientConstants.LOADERS_HOLDER;
    }

    @Override
    public @Nonnull ResourceLocation getModelLocation() {
        return model;
    }

    @Override
    public @Nonnull Map<BiomeOverlayInstance, ResourceLocation> getBiomeTextureResourceMap() {
        return biomeTextureResourceMap;
    }

    @Override
    public @Nonnull List<BiomeOverlayInstance> getNonExistingTexturesReported() {
        return nonExistingReported;
    }
}
