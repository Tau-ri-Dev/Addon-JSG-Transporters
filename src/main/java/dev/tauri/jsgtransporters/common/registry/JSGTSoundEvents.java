package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.sound.SoundEvent;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTSoundEvents {
    public static final SoundEvent RINGS_TRANSPORT_START = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.transport.start"), 1, 110).register(JSGTransporters.REGISTRY_HELPER.sound());
    public static final SoundEvent RINGS_TRANSPORT_END = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.transport.end"), 1, 90).register(JSGTransporters.REGISTRY_HELPER.sound());

    public static final SoundEvent RINGS_GOAULD_BUTTON = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.goauld.button"), 1, 10).register(JSGTransporters.REGISTRY_HELPER.sound());
    public static final SoundEvent RINGS_GOAULD_BUTTON_DIAL = new SoundEvent(JSGMapping.rl(JSGTransporters.MOD_ID, "block.rings.goauld.dial"), 1, 10).register(JSGTransporters.REGISTRY_HELPER.sound());

    public static void init() {
    }
}
