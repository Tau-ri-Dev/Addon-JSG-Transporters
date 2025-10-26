package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.stargate.ScheduledTaskType;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

public class RingsScheduledTaskType {
    public static final ScheduledTaskType RINGS_SYMBOL_DEACTIVATE = new ScheduledTaskType(new ResourceLocation(JSGTransporters.MOD_ID, "rings_symbol_deactivate"), -1);
    public static final ScheduledTaskType RINGS_START_ANIMATION = new ScheduledTaskType(new ResourceLocation(JSGTransporters.MOD_ID, "rings_start_animation"), -1);
    public static final ScheduledTaskType RINGS_SOLID_BLOCKS = new ScheduledTaskType(new ResourceLocation(JSGTransporters.MOD_ID, "rings_solid_blocks"), -1);

    public static void load() {
    }
}
