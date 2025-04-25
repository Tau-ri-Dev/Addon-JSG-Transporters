package dev.tauri.jsgtransporters.common.integration.cctweaked;

import dev.tauri.jsg.integration.cctweaked.CCDevice;
import dev.tauri.jsgtransporters.common.integration.cctweaked.methods.RingsCCMethods;

public class CCDevicesRegistry {
    public static CCDevice RINGS = new CCDevice("RINGS", "rings", RingsCCMethods::new);

    public static void load() {
    }
}
