package dev.tauri.jsgtransporters.client;

import dev.tauri.jsg.api.client.IModelsHolder;
import dev.tauri.jsg.api.client.LoadersHolder;
import dev.tauri.jsg.api.registry.BiomeOverlayRegistry;
import dev.tauri.jsgtransporters.Constants;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ModelsHolder implements IModelsHolder {
    RING_GOAULD("rings/rings_goauld.obj", "rings/rings_goauld.jpg", false),
    RING_ANCIENT("rings/rings_ancient.obj", "rings/rings_ancient.jpg", false),
    RING_ORI("rings/rings_ori.obj", "rings/rings_ori.jpg", false),

    RINGS_CONTROLLER_GOAULD("rings/controller/goauld/plate_goauld.obj", "rings/controller/goauld/goauld_panel.jpg", true),
    RINGS_CONTROLLER_GOAULD_LIGHT("rings/controller/goauld/indicator_lights.obj", "rings/controller/goauld/goauld_light.jpg", true),
    RINGS_CONTROLLER_ORI_BASE("rings/controller/ori/base.obj", "rings/controller/ori/base.png", false),
    RINGS_CONTROLLER_ANCIENT_BASE("rings/controller/ancient/body_ancient.obj", "rings/controller/ancient/base.png", false),
    ;
    public final ResourceLocation model;
    public final Map<BiomeOverlayRegistry.BiomeOverlayInstance, ResourceLocation> biomeTextureResourceMap = new HashMap<>();
    private final List<BiomeOverlayRegistry.BiomeOverlayInstance> nonExistingReported = new ArrayList<>();

    ModelsHolder(String modelPath, String texturePath, boolean byOverlay) {
        this.model = Constants.LOADERS_HOLDER.model().getModelResource(modelPath);
        loadEntry(texturePath, byOverlay);
    }

    @Override
    public @NotNull LoadersHolder getLoadersHolder() {
        return Constants.LOADERS_HOLDER;
    }

    @Override
    public @NotNull ResourceLocation getModelLocation() {
        return model;
    }

    @Override
    public @NotNull Map<BiomeOverlayRegistry.BiomeOverlayInstance, ResourceLocation> getBiomeTextureResourceMap() {
        return biomeTextureResourceMap;
    }

    @Override
    public @NotNull List<BiomeOverlayRegistry.BiomeOverlayInstance> getNonExistingTexturesReported() {
        return nonExistingReported;
    }
}
