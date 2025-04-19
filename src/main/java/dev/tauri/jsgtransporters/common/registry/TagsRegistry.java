package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TagsRegistry {
    public static final TagKey<Block> UNTRANSPORTABLE_BLOCK = block("untransportable");
    public static final TagKey<Block> RINGS_BLOCK = block("rings_block");
    public static final TagKey<Block> ATLANTIS_TRANSPORTER_BLOCK = block("atlantis_transporter_block");
    public static final TagKey<Block> RINGS_CONTROLLER_BLOCK = block("rings_controllers");
    public static final TagKey<Block> OBELISK_BLOCK = block("obelisk_block");
    public static final TagKey<Block> TRANSPORTER_FLUIDS = block("transporter_fluids");

    private static TagKey<Block> block(String name) {
        return BlockTags.create(new ResourceLocation(JSGTransporters.MOD_ID, name));
    }
}
