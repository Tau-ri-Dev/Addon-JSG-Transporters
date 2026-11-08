package dev.tauri.jsgtransporters.common.registry.tags;

import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class JSGTItemTags {
    public static final TagKey<Item> RINGS_ENERGY_CRYSTALS = tag("rings_energy_crystals");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(JSGMapping.rl(JSGTransporters.MOD_ID, name));
    }
}
