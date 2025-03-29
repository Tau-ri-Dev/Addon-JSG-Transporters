package dev.tauri.jsgTransporters.client.loader;

import dev.tauri.jsg.api.loader.model.APIOBJLoader;
import dev.tauri.jsg.api.loader.texture.APITextureLoader;
import dev.tauri.jsgTransporters.JSGTransporters;

public class LoadersHolder {
    public static final APITextureLoader TEXTURE_LOADER = APITextureLoader.createLoader(JSGTransporters.MOD_ID, JSGTransporters.class);
    public static final APIOBJLoader MODEL_LOADER = APIOBJLoader.createLoader(JSGTransporters.MOD_ID, JSGTransporters.class);
}
