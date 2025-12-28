package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.api.item.JSGItem;
import dev.tauri.jsg.helpers.TabHelper;
import dev.tauri.jsg.registry.TabRegistry;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static dev.tauri.jsgtransporters.JSGTransporters.MOD_ID;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    /**
     * Icons used in advancements
     */

    public static final RegistryObject<JSGItem> ICON_JSG_TRANSPORTERS_LOGO = Constants.ITEM_HELPER.builder("icon_jsg_transporters").clearTooltip().setInTabs(List.of()).buildGeneric();

    /**
     * Glyph crystals
     */
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ANCIENT = Constants.ITEM_HELPER.builder("crystal_glyph_rings_ancient").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.ANCIENT_GLYPHS);
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_GOAULD = Constants.ITEM_HELPER.builder("crystal_glyph_rings_goauld").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.GOAULD_GLYPHS);
    public static final RegistryObject<JSGItem> CRYSTAL_GLYPH_ORI = Constants.ITEM_HELPER.builder("crystal_glyph_rings_ori").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.ORI_GLYPHS);
    public static final RegistryObject<JSGItem> CRYSTAL_UPGRADE_DIM_TUNNELING = Constants.ITEM_HELPER.builder("crystal_upgrade_dimensional_tunneling").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS, TabRegistry.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.DIMENSIONAL_TUNNELING);

    /**
     * Ring fragments
     */

    public static final RegistryObject<JSGItem> FRAGMENT_GOAULD = Constants.ITEM_HELPER.builder("fragment_rings_goauld").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();
    public static final RegistryObject<JSGItem> FRAGMENT_ORI = Constants.ITEM_HELPER.builder("fragment_rings_ori").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();
    public static final RegistryObject<JSGItem> FRAGMENT_ANCIENT = Constants.ITEM_HELPER.builder("fragment_rings_ancient").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();

    /**
     * Crafting elements
     */

    public static final RegistryObject<JSGItem> GOAULD_BUTTONS = Constants.ITEM_HELPER.builder("goauld_controller_buttons").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();
    public static final RegistryObject<JSGItem> ORI_BUTTONS = Constants.ITEM_HELPER.builder("ori_controller_buttons").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();
    public static final RegistryObject<JSGItem> ORI_MAIN_BUTTON = Constants.ITEM_HELPER.builder("ori_main_controller_button").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();
    public static final RegistryObject<JSGItem> ANCIENT_BUTTONS = Constants.ITEM_HELPER.builder("ancient_controller_buttons").setInTabs(List.of(dev.tauri.jsgtransporters.common.registry.TabRegistry.TAB_RINGS)).buildGeneric();


    public static void register(IEventBus bus) {
        TabHelper.indexItemRegistry(() -> REGISTER);
        REGISTER.register(bus);
    }
}
