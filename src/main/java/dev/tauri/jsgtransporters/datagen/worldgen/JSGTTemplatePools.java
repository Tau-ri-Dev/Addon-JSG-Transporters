package dev.tauri.jsgtransporters.datagen.worldgen;

import com.mojang.datafixers.util.Pair;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.worldgen.JSGTProcessorList;
import dev.tauri.jsgtransporters.common.registry.worldgen.JSGTTemplatePool;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;


public class JSGTTemplatePools {
    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {

        var pools = context.lookup(Registries.TEMPLATE_POOL);
        var processors = context.lookup(Registries.PROCESSOR_LIST);
        var emptyPool = pools.getOrThrow(Pools.EMPTY);

        //ABYDOS PYRAMID GATEROOM RING CONTROL PANELS
        context.register(JSGTTemplatePool.GATEROOM_RING_PANELS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":transport_rings/common/panels/goauld_panel"), 4)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID GATEROOM TRANSPORT RING
        context.register(JSGTTemplatePool.GATEROOM_RINGS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":transport_rings/abydos/ring_blocks/goauld_rings"), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID GATEROOM CELLING RING COVERS
        context.register(JSGTTemplatePool.RING_CEILING_COVERS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":abydos/main_pyramid/gateroom_ring_cover"), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID TOP - STRUCTURE WITH HIDDEN - EXTRA TREASURE ROOM
        context.register(JSGTTemplatePool.TOPS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":abydos/main_pyramid/top",
                        processors.getOrThrow(JSGTProcessorList.PHYSICAL_TREASURE)), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID TREASURE ROOM RING CONTROL PANELS
        context.register(JSGTTemplatePool.TREASURE_ROOM_RING_PANELS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":transport_rings/common/panels/goauld_panel"), 9)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID TREASURE ROOM TRANSPORT RING
        context.register(JSGTTemplatePool.TREASURE_ROOM_RINGS, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":transport_rings/common/ring_blocks/goauld_rings"), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

        //ABYDOS PYRAMID TREASURE ROOM CHEST LOOT
        context.register(JSGTTemplatePool.SECRET_RING_TREASURE, new StructureTemplatePool(
                emptyPool,
                List.of(Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":containers/abydos/main_pyramid/secret_ring_treasure/barrel_up"), 1),
                        Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":containers/abydos/main_pyramid/secret_ring_treasure/barrel_side"), 1),
                        Pair.of(StructurePoolElement.single(JSGTransporters.MOD_ID + ":containers/abydos/main_pyramid/secret_ring_treasure/chest"), 1)),
                StructureTemplatePool.Projection.RIGID
        ));

    }
}
