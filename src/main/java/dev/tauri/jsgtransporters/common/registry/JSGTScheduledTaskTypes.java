package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.entity.ScheduledTaskType;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraftforge.registries.RegistryObject;

public class JSGTScheduledTaskTypes {
    public static final RegistryObject<ScheduledTaskType> RINGS_SYMBOL_DEACTIVATE = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_symbol_deactivate", () -> new ScheduledTaskType("rings_symbol_deactivate", -1));
    public static final RegistryObject<ScheduledTaskType> RINGS_START_ANIMATION = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_start_animation", () -> new ScheduledTaskType("rings_start_animation", -1));
    public static final RegistryObject<ScheduledTaskType> RINGS_SOLID_BLOCKS = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_solid_blocks", () -> new ScheduledTaskType("rings_solid_blocks", -1));


    public static void init() {
    }
}
