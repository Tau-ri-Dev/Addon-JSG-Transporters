package dev.tauri.jsgtransporters.common.integration.oc2;

import dev.tauri.jsg.integration.oc2.OCDevice;
import dev.tauri.jsgtransporters.common.integration.oc2.methods.RingsOCMethods;

public class OCDevicesRegistry {
    public static OCDevice RINGS = new OCDevice("RINGS", "rings", RingsOCMethods::new);

    public static void load() {
    }
}
