package dev.tauri.jsgTransporters.common.registry;

import static dev.tauri.jsgTransporters.JSGTransporters.MOD_ID;
import dev.tauri.jsgTransporters.common.block.rings.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
  public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
  /**
   * TRANSPORT RINGS
   */
  public static final RegistryObject<Block> RING_ANCIENT = REGISTER.register("ring_ancient", RingsAncient::new);
  public static final RegistryObject<Block> RING_GOAULD = REGISTER.register("ring_goauld", RingsGoauld::new);
  public static final RegistryObject<Block> RING_ORI = REGISTER.register("ring_ori", RingsOri::new);
  /**
   * ATLANTIS TRANSPORTER
   */

  /**
   * OBELISK TRANSPORTER
   */
}
