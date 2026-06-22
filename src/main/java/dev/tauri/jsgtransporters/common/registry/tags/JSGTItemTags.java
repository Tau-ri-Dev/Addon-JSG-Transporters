package dev.tauri.jsgtransporters.common.registry.tags;

import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class JSGTItemTags {
    public static final TagKey<Item> RINGS_CAPACITORS = tag("rings_capacitors");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(JSGMapping.rl(JSGTransporters.MOD_ID, name));
    }
}
