package dev.tauri.jsgtransporters.common.config;

import net.minecraft.resources.ResourceLocation;

import static dev.tauri.jsg.config.ingame.BlockConfigOptionRegistry.register;
import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

public class BlockConfigOptionRegistry {
    public static final ResourceLocation RINGS_COMMON = register(new ResourceLocation(MOD_ID, "rings_common"), () -> () -> RingsConfigOptions.COMMON);
}
