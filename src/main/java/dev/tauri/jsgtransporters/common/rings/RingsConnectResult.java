package dev.tauri.jsgtransporters.common.rings;

public enum RingsConnectResult {
    OK,
    ADDRESS_MALFORMED,
    NO_POWER,
    NO_ORIGIN,
    BUSY,
    OBFUSCATED,
    OBFUSCATED_TARGET,

    CLIENT;

    public boolean ok() {
        return this == OK;
    }
}
