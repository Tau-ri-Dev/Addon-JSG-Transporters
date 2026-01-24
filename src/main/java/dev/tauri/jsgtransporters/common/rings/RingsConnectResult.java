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
    OBSTRUCTED,
    OBSTRUCTED_TARGET,

    CLIENT;
    /**
     * @deprecated use {@link #OBSTRUCTED} instead
     */
    @Deprecated(forRemoval = true)
    public static final RingsConnectResult OBFUSCATED = OBSTRUCTED;
    /**
     * @deprecated use {@link #OBSTRUCTED_TARGET} instead
     */
    @Deprecated(forRemoval = true)
    public static final RingsConnectResult OBFUSCATED_TARGET = OBSTRUCTED_TARGET;

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
