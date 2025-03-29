package dev.tauri.jsgTransporters.common.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.tauri.jsgTransporters.JSGTransporters.MOD_ID;
import dev.tauri.jsgTransporters.common.helpers.ItemHelper;

import dev.tauri.jsg.item.JSGItem;
import dev.tauri.jsg.registry.TabRegistry;

public class ItemRegistry {
  public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

  /**
   * TRANSPORT RINGS
   */
  /**
   * Glyph crystals
   */
  public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ANCIENT = ItemHelper.createGenericItemWithGenericTooltip(
      "crystal_glyph_rings_ancient", TabRegistry.TAB_UPGRADES);
  public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_GOAULD = ItemHelper.createGenericItemWithGenericTooltip(
      "crystal_glyph_rings_goauld", TabRegistry.TAB_UPGRADES);
  public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ORI = ItemHelper.createGenericItemWithGenericTooltip(
      "crystal_glyph_rings_ori", TabRegistry.TAB_UPGRADES);

  /**
   * Schematics
   */
  public static final RegistryObject<JSGItem> SCHEMATIC_ANCIENT = ItemHelper.createGenericItemWithGenericTooltip(
      "schematic_rings_ancient", TabRegistry.TAB_UPGRADES);
  public static final RegistryObject<JSGItem> SCHEMATIC_GOAULD = ItemHelper.createGenericItemWithGenericTooltip(
      "schematic_rings_goauld", TabRegistry.TAB_UPGRADES);
  public static final RegistryObject<JSGItem> SCHEMATIC_ORI = ItemHelper.createGenericItemWithGenericTooltip(
      "schematic_rings_ori", TabRegistry.TAB_UPGRADES);

}
