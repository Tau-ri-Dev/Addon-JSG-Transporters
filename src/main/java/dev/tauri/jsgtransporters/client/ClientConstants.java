package dev.tauri.jsgtransporters.client;

import dev.tauri.jsg.api.client.LoadersHolder;
import dev.tauri.jsgtransporters.JSGTransporters;

public class ClientConstants {
    // contains instances of model and texture loaders for this addon
    public static final LoadersHolder LOADERS_HOLDER = LoadersHolder.getOrCreate(JSGTransporters.MOD_ID, JSGTransporters.class);

    public static void load(){}
}
