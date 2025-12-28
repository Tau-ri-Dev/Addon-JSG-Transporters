package dev.tauri.jsgtransporters.common.config;


import dev.tauri.jsg.api.config.ingame.option.JSGIntRangeConfigOption;
import dev.tauri.jsg.api.config.ingame.options.BlockConfigOptions;

import java.util.List;

public interface RingsConfigOptions extends BlockConfigOptions {

    List<RingsConfigOptions> COMMON = List.of(
            () -> new JSGIntRangeConfigOption("maxCapacitors", 0, 3, 2,
                    "Specifies how many",
                    "capacitors can be installed",
                    "in these rings"
            )
    );
}