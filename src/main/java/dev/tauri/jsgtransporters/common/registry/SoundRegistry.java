package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.sound.SoundPositionedEnum;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JSGTransporters.MOD_ID);

    public static final SoundPositionedEnum RINGS_TRANSPORT = new SoundPositionedEnum(JSGTransporters.MOD_ID, "block.rings.transport", false, 1).register(() -> REGISTER);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
