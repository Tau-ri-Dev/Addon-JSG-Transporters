package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.helpers.TabHelper;
import dev.tauri.jsg.item.JSGItem;
import dev.tauri.jsg.registry.TabRegistry;
import dev.tauri.jsgtransporters.Constants;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    /**
     * Glyph crystals
     */
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ANCIENT = Constants.ITEM_HELPER.createGenericItemWithGenericTooltip(
            "crystal_glyph_rings_ancient", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES));
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_GOAULD = Constants.ITEM_HELPER.createGenericItemWithGenericTooltip(
            "crystal_glyph_rings_goauld", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES));
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ORI = Constants.ITEM_HELPER.createGenericItemWithGenericTooltip(
            "crystal_glyph_rings_ori", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES));
    public static final RegistryObject<JSGItem> CRYSTAL_UPGRADE_DIM_TUNNELING = Constants.ITEM_HELPER.createGenericItemWithGenericTooltip(
            "crystal_upgrade_dimensional_tunneling", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES));

    /**
     * Ring fragments
     */
    public static final RegistryObject<JSGItem> FRAGMENT_GOAULD = Constants.ITEM_HELPER.createGenericItem("fragment_rings_goauld", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));
    public static final RegistryObject<JSGItem> FRAGMENT_ORI = Constants.ITEM_HELPER.createGenericItem("fragment_rings_ori", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));
    public static final RegistryObject<JSGItem> FRAGMENT_ANCIENT = Constants.ITEM_HELPER.createGenericItem("fragment_rings_ancient", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));

    /**
     * Crafting elements
     */

    public static final RegistryObject<JSGItem> GOAULD_BUTTONS = Constants.ITEM_HELPER.createGenericItem("goauld_controller_buttons", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));

    /**

     prepared registration of new crafting elements

    public static final RegistryObject<JSGItem> ORI_BUTTONS = Constants.ITEM_HELPER.createGenericItem("ori_controller_buttons", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));
    public static final RegistryObject<JSGItem> ORI_MAIN_BUTTON = Constants.ITEM_HELPER.createGenericItem("ori_main_controller_button", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));
    public static final RegistryObject<JSGItem> ANCIENT_BUTTONS = Constants.ITEM_HELPER.createGenericItem("ancient_controller_buttons", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));
    public static final RegistryObject<JSGItem> ORI_CONTROLLER_BASE = Constants.ITEM_HELPER.createGenericItem("ori_controller_base", List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS));

    */

    public static void register(IEventBus bus) {
        TabHelper.indexItemRegistry(() -> REGISTER);
        REGISTER.register(bus);
    }
}
