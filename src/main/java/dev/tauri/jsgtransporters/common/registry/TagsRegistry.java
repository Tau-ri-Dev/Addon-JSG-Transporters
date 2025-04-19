package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagsRegistry {
    public static final TagKey<Block> UNTRANSPORTABLE_BLOCK = block("untransportable");
    public static final TagKey<Block> RINGS_BLOCK = block("rings_block");
    public static final TagKey<Block> ATLANTIS_TRANSPORTER_BLOCK = block("atlantis_transporter_block");
    public static final TagKey<Block> RINGS_CONTROLLER_BLOCK = block("rings_controllers");
    public static final TagKey<Block> OBELISK_BLOCK = block("obelisk_block");


    public static final TagKey<Fluid> TRANSPORTER_FLUIDS = fluid("transporter_fluids");

    private static TagKey<Block> block(String name) {
        return BlockTags.create(new ResourceLocation(JSGTransporters.MOD_ID, name));
    }

    private static TagKey<Fluid> fluid(String name) {
      return FluidTags.create(new ResourceLocation(JSGTransporters.MOD_ID, name));
    }
}
