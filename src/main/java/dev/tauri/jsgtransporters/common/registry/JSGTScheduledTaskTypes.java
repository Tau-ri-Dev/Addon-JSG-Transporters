package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.entity.ScheduledTaskType;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTScheduledTaskTypes {
    public static final RegistryObjectWrapper<ScheduledTaskType> RINGS_SYMBOL_DEACTIVATE = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_symbol_deactivate", () -> new ScheduledTaskType("rings_symbol_deactivate", -1));
    public static final RegistryObjectWrapper<ScheduledTaskType> RINGS_START_ANIMATION = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_start_animation", () -> new ScheduledTaskType("rings_start_animation", -1));
    public static final RegistryObjectWrapper<ScheduledTaskType> RINGS_SOLID_BLOCKS = JSGTransporters.REGISTRY_HELPER.scheduledTask().register("rings_solid_blocks", () -> new ScheduledTaskType("rings_solid_blocks", -1));


    public static void init() {
    }
}
