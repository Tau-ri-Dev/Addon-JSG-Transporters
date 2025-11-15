package dev.tauri.jsgtransporters.common.worldgen;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.worldgen.poolinject.TemplatePoolInjector;
import net.minecraft.resources.ResourceLocation;

public class StructuresInjector {
    public static void register() {
        new TemplatePoolInjector.Builder()
                .addPool(new ResourceLocation(JSG.MOD_ID, "abydos/main_pyramid/gateroom_ring_sections"))
                .setClearPool()
                .submit();
    }
}
