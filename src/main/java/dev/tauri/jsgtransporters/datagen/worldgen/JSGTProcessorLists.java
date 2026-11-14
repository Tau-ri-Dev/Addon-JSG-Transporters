package dev.tauri.jsgtransporters.datagen.worldgen;

import com.google.common.collect.ImmutableList;
import dev.tauri.jsgcore.common.definition.material.MaterialType;
import dev.tauri.jsgtransporters.common.registry.worldgen.JSGTProcessorList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

public class JSGTProcessorLists {

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        context.register(JSGTProcessorList.PHYSICAL_TREASURE, new StructureProcessorList(ImmutableList.of(
                new RuleProcessor(ImmutableList.of(
                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.RAW_GOLD_BLOCK, 0.5F),
                                AlwaysTrueTest.INSTANCE,
                                MaterialType.NAQUADAH.getRaw().get().defaultBlockState()
                        ),

                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.RAW_GOLD_BLOCK, 0.2F),
                                AlwaysTrueTest.INSTANCE,
                                MaterialType.NAQUADAH.getStorage().get().defaultBlockState()
                        ),

                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.RAW_GOLD_BLOCK, 0.1F),
                                AlwaysTrueTest.INSTANCE,
                                MaterialType.NAQUADAH_ALLOY.getStorage().get().defaultBlockState()
                        ),

                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.5F),
                                AlwaysTrueTest.INSTANCE,
                                MaterialType.NAQUADAH.getStorage().get().defaultBlockState()
                        ),

                        new ProcessorRule(
                                new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.1F),
                                AlwaysTrueTest.INSTANCE,
                                MaterialType.NAQUADAH_ALLOY.getStorage().get().defaultBlockState()
                        )
                ))
        )));
    }
}
