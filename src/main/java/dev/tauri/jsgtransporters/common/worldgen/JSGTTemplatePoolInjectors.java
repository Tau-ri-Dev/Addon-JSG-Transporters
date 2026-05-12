package dev.tauri.jsgtransporters.common.worldgen;

import dev.tauri.jsg.core.common.worldgen.TemplatePoolInjector;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTTemplatePoolInjectors {
    public static void register() {
        TemplatePoolInjector.Builder.forTargets(JSGMapping.rl("jsg", "abydos/main_pyramid/gateroom_ring_sections"))
                .add(new TemplatePoolInjector.ElementAddition(JSGMapping.rl(JSGTransporters.MOD_ID, "abydos/main_pyramid/gateroom_rings_section"), 1, false))
                .clearPoolFirst()
                .submit();

        TemplatePoolInjector.Builder.forTargets(JSGMapping.rl("jsg", "abydos/main_pyramid/tops"))
                .add(new TemplatePoolInjector.ElementAddition(JSGMapping.rl(JSGTransporters.MOD_ID, "abydos/main_pyramid/top"), 1, false))
                .clearPoolFirst()
                .submit();
    }
}
