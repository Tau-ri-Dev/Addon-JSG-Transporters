package dev.tauri.jsgtransporters.common.advancements;

import dev.tauri.jsg.core.common.advancement.JSGCriterion;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;

public class JSGTAdvancements {

    /* ACTIVATION EVENT - full address (not nearest device) */

    public static JSGCriterion ACTIVATED_GOAULD = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_goauld"));
    public static JSGCriterion ACTIVATED_ORI = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_ori"));
    public static JSGCriterion ACTIVATED_ANCIENT = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_ancient"));
    public static JSGCriterion ACTIVATED_TRANSPORTER = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_transporter"));
    public static JSGCriterion ACTIVATED_OBELISK = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "activated_obelisk"));
    public static JSGCriterion DIMENSIONAL_TRANSPORT = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "dimensional_transport"));

    /* MERGING OF TELEPORT DEVICE */
    public static JSGCriterion MERGED_TRANSPORTER = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "merged_transporter"));
    public static JSGCriterion MERGED_OBELISK = new JSGCriterion(JSGMapping.rl(JSGTransporters.MOD_ID, "merged_obelisk"));


    public static void init() {
    }
}