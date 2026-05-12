package dev.tauri.jsgtransporters.common.registry.tags;

import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class JSGTFluidTags {
    public static final TagKey<Fluid> TRANSPORTER_FLUIDS = tag("transporter_fluids");

    private static TagKey<Fluid> tag(String name) {
        return FluidTags.create(new ResourceLocation(JSGTransporters.MOD_ID, name));
    }
}
