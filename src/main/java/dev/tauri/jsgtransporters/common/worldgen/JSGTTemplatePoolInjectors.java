package dev.tauri.jsgtransporters.common.worldgen;

import dev.tauri.jsgcore.common.worldgen.TemplatePoolInjector;
import dev.tauri.jsgcore.mapping.JSGMapping;

public class JSGTTemplatePoolInjectors {
    public static void register() {
        TemplatePoolInjector.Builder
                .forTargets(JSGMapping.rl("jsg:abydos/main_pyramid/gateroom_ring_panels"))
                .clearPoolFirst()
                .add(new TemplatePoolInjector.PoolAddition(JSGMapping.rl("jsg_transporters:main_pyramid/gateroom_ring_panels")))
                .submit();

        TemplatePoolInjector.Builder
                .forTargets(JSGMapping.rl("jsg:abydos/main_pyramid/gateroom_rings"))
                .clearPoolFirst()
                .add(new TemplatePoolInjector.PoolAddition(JSGMapping.rl("jsg_transporters:main_pyramid/gateroom_rings")))
                .submit();

        TemplatePoolInjector.Builder
                .forTargets(JSGMapping.rl("jsg:abydos/main_pyramid/ring_ceiling_covers"))
                .clearPoolFirst()
                .add(new TemplatePoolInjector.PoolAddition(JSGMapping.rl("jsg_transporters:main_pyramid/ring_ceiling_covers")))
                .submit();

        TemplatePoolInjector.Builder
                .forTargets(JSGMapping.rl("jsg:abydos/main_pyramid/tops"))
                .clearPoolFirst()
                .add(new TemplatePoolInjector.PoolAddition(JSGMapping.rl("jsg_transporters:main_pyramid/tops")))
                .submit();
    }
}
