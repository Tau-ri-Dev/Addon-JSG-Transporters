package dev.tauri.jsgtransporters.common.integration.oc;

import dev.tauri.jsgcore.common.integration.oc.OCDevice;
import dev.tauri.jsgtransporters.common.integration.oc.methods.RingsOCMethods;

public class OCDevicesRegistry {
    public static OCDevice RINGS = new OCDevice("RINGS", "rings", RingsOCMethods::new);

    public static void load() {
    }
}
