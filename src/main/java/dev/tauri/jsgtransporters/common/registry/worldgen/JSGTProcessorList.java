package dev.tauri.jsgtransporters.common.registry.worldgen;

import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;


public class JSGTProcessorList {
    public static final ResourceKey<StructureProcessorList> PHYSICAL_TREASURE = registerKey("abydos/physical_treasure");

    private static ResourceKey<StructureProcessorList> registerKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, JSGMapping.rl(JSGTransporters.MOD_ID, name));
    }


}
