package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.registry.CoreTabs;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.advancements.JSGTAdvancements;
import net.minecraftforge.eventbus.api.IEventBus;

public class JSGTRegistriesInit {
    public static void init() {
        CoreTabs.registerTransportationTab(() -> JSGTBlocks.RINGS_GOAULD);

        JSGTAdvancements.init();
        JSGTSymbolTypes.init();
        JSGTScheduledTaskTypes.init();
        JSGTSymbolUsages.init();
        JSGTNotebookPageTypes.init();
        JSGTSoundEvents.init();
        JSGTRaycasters.init();
        JSGTBlocks.init();
        JSGTItems.init();
        JSGTTabs.init();
        JSGTBlockEntities.init();
        JSGTMenuTypes.init();
    }

    public static void register(IEventBus bus) {
        JSGTransporters.REGISTRY_HELPER.register(bus);
    }
}
