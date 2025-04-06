package dev.tauri.jsgtransporters.common.config;

import dev.tauri.jsg.config.ingame.BlockConfigOptions;
import dev.tauri.jsg.config.ingame.JSGBooleanConfigOption;

import java.util.List;

public interface RingsConfigOptions extends BlockConfigOptions {

    List<RingsConfigOptions> COMMON = List.of(
            () -> new JSGBooleanConfigOption("test", false,
                    "Spagety smrdí")
    );
}