package dev.tauri.jsgtransporters.datagen.worldgen;

import com.google.common.collect.ImmutableList;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

public class JSGTProcessorLists {
    public static ResourceKey<StructureProcessorList> registerKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, JSGMapping.rl(JSGTransporters.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<StructureProcessorList> context) {
        context.register(registerKey("add_cobweb"), new StructureProcessorList(ImmutableList.of(
                new RuleProcessor(ImmutableList.of(
                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.AIR, 0.3f),
                                AlwaysTrueTest.INSTANCE,
                                PosAlwaysTrueTest.INSTANCE,
                                Blocks.COBWEB.defaultBlockState()
                        )
                ))
        )));
    }
}
