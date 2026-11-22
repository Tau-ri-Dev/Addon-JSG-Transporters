package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.sound.SoundEvent;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTSoundEvents {
    public static final SoundEvent RINGS_TRANSPORT_START = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.transport.start"), 1, 110).register(JSGTransporters.REGISTRY_HELPER.sound());
    public static final SoundEvent RINGS_TRANSPORT_END = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.transport.end"), 1, 90).register(JSGTransporters.REGISTRY_HELPER.sound());

    public static final SoundEvent RINGS_CONTROL_PANEL_BUTTON = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.control_panel.button"), 1, 10).register(JSGTransporters.REGISTRY_HELPER.sound());
    public static final SoundEvent RINGS_CONTROL_PANEL_BUTTON_DIAL = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.control_panel.dial"), 1, 10).register(JSGTransporters.REGISTRY_HELPER.sound());

    public static void init() {
    }
}
