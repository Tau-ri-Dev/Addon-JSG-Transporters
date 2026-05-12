package dev.tauri.jsgtransporters.common.registry.tags;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class JSGTBlockTags {
    public static final TagKey<Block> UNTRANSPORTABLE_BLOCK = tag("untransportable");
    public static final TagKey<Block> RINGS_BLOCK = tag("rings_block");
    public static final TagKey<Block> ATLANTIS_TRANSPORTER_BLOCK = tag("atlantis_transporter_block");
    public static final TagKey<Block> RINGS_CONTROLLER_BLOCK = tag("rings_controllers");
    public static final TagKey<Block> OBELISK_BLOCK = tag("obelisk_block");

    public static final TagKey<Block> RINGS_GOAULD_LINKABLE = tag("rings/rings_goauld_linkable");
    public static final TagKey<Block> RINGS_ANCIENT_LINKABLE = tag("rings/rings_ancient_linkable");
    public static final TagKey<Block> RINGS_ORI_LINKABLE = tag("rings/rings_ori_linkable");

    public static final TagKey<Block> PANEL_GOAULD_LINKABLE = tag("panels/panel_goauld_linkable");
    public static final TagKey<Block> PANEL_ANCIENT_LINKABLE = tag("panels/panel_ancient_linkable");
    public static final TagKey<Block> PANEL_ORI_LINKABLE = tag("panels/panel_ori_linkable");

    private static TagKey<Block> tag(String name) {
        return BlockTags.create(new ResourceLocation(JSGTransporters.MOD_ID, name));
    }
}
