package dev.tauri.jsgtransporters.common.advancements;

import dev.tauri.jsg.core.common.advancement.JSGAdvancement;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.advancements.CriteriaTriggers;

public class JSGTAdvancements {

    /* ACTIVATION EVENT - full address (not nearest device) */

    public static JSGAdvancement ACTIVATED_GOAULD = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_goauld"));
    public static JSGAdvancement ACTIVATED_ORI = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_ori"));
    public static JSGAdvancement ACTIVATED_ANCIENT = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_ancient"));
    public static JSGAdvancement ACTIVATED_TRANSPORTER = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_transporter"));
    public static JSGAdvancement ACTIVATED_OBELISK = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_obelisk"));
    public static JSGAdvancement DIMENSIONAL_TRANSPORT = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "dimensional_transport"));

    /* MERGING OF TELEPORT DEVICE */
    public static JSGAdvancement MERGED_TRANSPORTER = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "merged_transporter"));
    public static JSGAdvancement MERGED_OBELISK = new JSGAdvancement(JSGMapping.rl(JSGTransporters.MOD_ID, "merged_obelisk"));


    public static final JSGAdvancement[] TRIGGER_ARRAY = new JSGAdvancement[]{
            ACTIVATED_GOAULD,
            ACTIVATED_ORI,
            ACTIVATED_ANCIENT,
            ACTIVATED_TRANSPORTER,
            ACTIVATED_OBELISK,
            DIMENSIONAL_TRANSPORT,

            MERGED_TRANSPORTER,
            MERGED_OBELISK
    };

    public static void register() {
        for (int i = 0; i < JSGTAdvancements.TRIGGER_ARRAY.length; i++) {
            JSGAdvancement a = JSGTAdvancements.TRIGGER_ARRAY[i];
            CriteriaTriggers.register(a);
        }
        JSGTransporters.logger.info("Successfully registered Advancement Triggers!");
    }
}