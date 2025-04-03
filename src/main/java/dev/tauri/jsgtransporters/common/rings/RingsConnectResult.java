package dev.tauri.jsgtransporters.common.rings;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.network.chat.Component;

public enum RingsConnectResult {
    OK,
    ADDRESS_MALFORMED,
    OUT_OF_RANGE,
    NO_POWER,
    NO_ORIGIN,
    BUSY,
    BUSY_TARGET,
    OBFUSCATED,
    OBFUSCATED_TARGET,

    CLIENT;

    public final String translationKey;

    RingsConnectResult() {
        this.translationKey = "block." + JSGTransporters.MOD_ID + ".rings.message." + name().toLowerCase();
    }

    public Component component() {
        return Component.translatable(translationKey);
    }

    public boolean ok() {
        return this == OK;
    }
}
