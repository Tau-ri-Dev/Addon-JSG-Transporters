package dev.tauri.jsgTransporters.common.registry;

import static dev.tauri.jsgTransporters.JSGTransporters.MOD_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dev.tauri.jsgTransporters.common.blockentity.rings.RingsAnceintBE;
import dev.tauri.jsgTransporters.common.blockentity.rings.RingsGoauldBE;
import dev.tauri.jsgTransporters.common.blockentity.rings.RingsOriBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
  public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister
      .create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

  /**
   * TRANSPORT RINGS
   */
  public static final RegistryObject<BlockEntityType<RingsAnceintBE>> RINGS_ANCIENT_BE = registerBR("ring_ancient",
      RingsAnceintBE::new, BlockRegistry.RING_ANCIENT);
  public static final RegistryObject<BlockEntityType<RingsGoauldBE>> RINGS_GOAULD_BE = registerBR("ring_goauld",
      RingsGoauldBE::new, BlockRegistry.RING_GOAULD);
      public static final RegistryObject<BlockEntityType<RingsOriBE>> RINGS_ORI_BE = registerBR("ring_ori",
      RingsOriBE::new, BlockRegistry.RING_ORI);











      public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBR(String name,
      BlockEntityType.BlockEntitySupplier<T> beSupplier, Supplier<? extends Block> blockSupplier) {
    return registerBR(name, beSupplier, List.of(blockSupplier));
  }

  public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBR(String name,
      BlockEntityType.BlockEntitySupplier<T> beSupplier, List<? extends Supplier<? extends Block>> blockSuppliers) {
    return REGISTER.register(name, () -> {
      List<Block> blocks = new ArrayList<>();
      for (var object : blockSuppliers) {
        blocks.add(object.get());
      }
      return BlockEntityType.Builder.of(beSupplier, blocks.toArray(new Block[0])).build(null);
    });
  }
}
