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


    public static void register(IEventBus bus) {
        TabHelper.indexItemRegistry(() -> REGISTER);
        REGISTER.register(bus);
    }
}
