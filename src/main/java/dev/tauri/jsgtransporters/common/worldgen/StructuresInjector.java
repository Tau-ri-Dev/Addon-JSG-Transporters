package dev.tauri.jsgtransporters.common.worldgen;

import dev.tauri.jsg.JSG;
import dev.tauri.jsg.worldgen.poolinject.TemplatePoolInjector;
import net.minecraft.resources.ResourceLocation;

public class StructuresInjector {
    public static void register() {
        new TemplatePoolInjector.Builder()
                .addPool(new ResourceLocation(JSG.MOD_ID, "abydos/main_pyramid/gateroom_ring_sections"))
                .setClearPool()
                .addAddition("jsg_transporters:abydos/main_pyramid/gateroom_rings_section",1)
                .submit();

        new TemplatePoolInjector.Builder()
                .addPool(new ResourceLocation(JSG.MOD_ID, "abydos/main_pyramid/tops"))
                .setClearPool()
                .addAddition("jsg_transporters:abydos/main_pyramid/top",1)
                .submit();
    }
}
