package dev.tauri.jsgTransporters.common.registry;

import static dev.tauri.jsg.config.ingame.BlockConfigOptionRegistry.register;
import static dev.tauri.jsgTransporters.JSGTransporters.MOD_ID;
import dev.tauri.jsgTransporters.common.config.RingsConfigOptions;

import net.minecraft.resources.ResourceLocation;

public class BlockConfigOptionRegistry {
  public static final ResourceLocation RINGS_COMMON = register(new ResourceLocation(MOD_ID, "rings_common"),
      RingsConfigOptions.COMMON);
}
