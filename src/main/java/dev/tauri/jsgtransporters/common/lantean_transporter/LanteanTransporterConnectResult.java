package dev.tauri.jsgtransporters.common.lantean_transporter;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.network.chat.Component;

public enum LanteanTransporterConnectResult {
    OK,
    NO_POWER,
    BUSY,
    BUSY_TARGET,
    OBSTRUCTED,
    OBSTRUCTED_TARGET,
    CLIENT;
    public final String translationKey;

    LanteanTransporterConnectResult() {
        this.translationKey = "block." + JSGTransporters.MOD_ID + ".lantean_transporter.message." + name().toLowerCase();
    }

    public Component coponent() {
        return Component.translatable(translationKey);
    }

    public boolean ok() {
        return this == OK;
    }
}
