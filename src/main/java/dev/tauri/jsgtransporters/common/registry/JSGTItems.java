package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.item.JSGItem;
import dev.tauri.jsgcore.common.registry.CoreTabs;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgtransporters.Constants;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;

import java.util.List;

public class JSGTItems {
    /**
     * Icons used in advancements
     */
    //public static final RegistryObjectWrapper<JSGItem> ICON_JSG_TRANSPORTERS_LOGO = Constants.ITEM_HELPER.builder("icon_jsg_transporters").clearTooltip().setInTabs(List.of()).buildGeneric();

    /**
     * Glyph crystals
     */
    public static final RegistryObjectWrapper<JSGItem> CRYSTAL_GLYPH_ANCIENT = Constants.ITEM_HELPER.builder("ancient_glyph_crystal").setInTabs(List.of(CoreTabs.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.ANCIENT_GLYPHS);
    public static final RegistryObjectWrapper<JSGItem> CRYSTAL_GLYPH_GOAULD = Constants.ITEM_HELPER.builder("goauld_glyph_crystal").setInTabs(List.of(CoreTabs.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.GOAULD_GLYPHS);
    public static final RegistryObjectWrapper<JSGItem> CRYSTAL_GLYPH_ORI = Constants.ITEM_HELPER.builder("ori_glyph_crystal").setInTabs(List.of(CoreTabs.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.ORI_GLYPHS);
    public static final RegistryObjectWrapper<JSGItem> CRYSTAL_UPGRADE_DIM_TUNNELING = Constants.ITEM_HELPER.builder("dimensional_tunneling_upgrade_crystal").setInTabs(List.of(CoreTabs.TAB_UPGRADES)).buildUpgrade(() -> RingsAbstractBE.RingsUpgradeEnum.DIMENSIONAL_TUNNELING);

    /**
     * Ring fragments
     */
    public static final RegistryObjectWrapper<JSGItem> FRAGMENT_GOAULD = Constants.ITEM_HELPER.builder("goauld_transport_ring_fragment").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();
    public static final RegistryObjectWrapper<JSGItem> FRAGMENT_ORI = Constants.ITEM_HELPER.builder("ori_transport_ring_fragment").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();
    public static final RegistryObjectWrapper<JSGItem> FRAGMENT_ANCIENT = Constants.ITEM_HELPER.builder("ancient_transport_ring_fragment").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();

    /**
     * Crafting elements
     */
    public static final RegistryObjectWrapper<JSGItem> GOAULD_BUTTONS = Constants.ITEM_HELPER.builder("goauld_controller_buttons").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();
    public static final RegistryObjectWrapper<JSGItem> ORI_BUTTONS = Constants.ITEM_HELPER.builder("ori_controller_buttons").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();
    public static final RegistryObjectWrapper<JSGItem> ORI_MAIN_BUTTON = Constants.ITEM_HELPER.builder("main_ori_controller_button").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();
    public static final RegistryObjectWrapper<JSGItem> ANCIENT_BUTTONS = Constants.ITEM_HELPER.builder("ancient_controller_buttons").setInTabs(List.of(() -> CoreTabs.TAB_RESOURCES)).clearTooltip().buildGeneric();


    public static void init() {
    }
}
