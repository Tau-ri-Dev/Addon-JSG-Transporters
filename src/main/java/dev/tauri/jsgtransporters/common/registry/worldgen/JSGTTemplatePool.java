package dev.tauri.jsgtransporters.common.registry.worldgen;

import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class JSGTTemplatePool {

    public static final ResourceKey<StructureTemplatePool> GATEROOM_RING_PANELS = createKey("abydos/main_pyramid/gateroom_ring_panels");
    public static final ResourceKey<StructureTemplatePool> GATEROOM_RINGS = createKey("abydos/main_pyramid/gateroom_rings");
    public static final ResourceKey<StructureTemplatePool> RING_CEILING_COVERS = createKey("abydos/main_pyramid/ring_ceiling_covers");
    public static final ResourceKey<StructureTemplatePool> TOPS = createKey("abydos/main_pyramid/tops");
    public static final ResourceKey<StructureTemplatePool> TREASURE_ROOM_RING_PANELS = createKey("abydos/main_pyramid/treasure_room_ring_panels");
    public static final ResourceKey<StructureTemplatePool> TREASURE_ROOM_RINGS = createKey("abydos/main_pyramid/treasure_room_rings");

    public static final ResourceKey<StructureTemplatePool> SECRET_RING_TREASURE = createKey("containers/abydos/main_pyramid/secret_ring_treasure");


    private static ResourceKey<StructureTemplatePool> createKey(String path) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, JSGMapping.rl(JSGTransporters.MOD_ID, path));
    }
}
