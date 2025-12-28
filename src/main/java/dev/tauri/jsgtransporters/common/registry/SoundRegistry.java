package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.sound.SoundEvent;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<net.minecraft.sounds.SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JSGTransporters.MOD_ID);

    public static final SoundEvent RINGS_TRANSPORT_START = new SoundEvent(JSGTransporters.MOD_ID, "block.rings.transport.start", 1, 110).register(() -> REGISTER);
    public static final SoundEvent RINGS_TRANSPORT_END = new SoundEvent(JSGTransporters.MOD_ID, "block.rings.transport.end", 1, 90).register(() -> REGISTER);


    public static final SoundEvent RINGS_GOAULD_BUTTON = new SoundEvent(JSGTransporters.MOD_ID, "block.rings.goauld.button", 1, 10).register(() -> REGISTER);
    public static final SoundEvent RINGS_GOAULD_BUTTON_DIAL = new SoundEvent(JSGTransporters.MOD_ID, "block.rings.goauld.dial", 1, 10).register(() -> REGISTER);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
