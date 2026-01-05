package dev.tauri.jsgtransporters.common.config.ingame;

import dev.tauri.jsg.api.config.ingame.BEConfigOptionProvider;
import dev.tauri.jsg.api.config.ingame.option.ConfigOptionsHolder;
import dev.tauri.jsg.api.config.ingame.option.type.IntegerBEConfigOption;

public class RingsConfigOptions {
    public static class Common {
        public static final ConfigOptionsHolder HOLDER = new ConfigOptionsHolder();

        public static final BEConfigOptionProvider<Integer> MAX_CAPACITORS = HOLDER.register("max_capacitors", (onChanged) -> new IntegerBEConfigOption(onChanged, 2, 0, 3));
    }
}
