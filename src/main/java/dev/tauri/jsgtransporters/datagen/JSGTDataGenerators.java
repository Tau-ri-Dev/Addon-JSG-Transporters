package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.datagen.*;
import dev.tauri.jsgcore.datagen.tag.*;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.datagen.loot.JSGTLootTableProvider;
import dev.tauri.jsgtransporters.datagen.tags.JSGTBlockTagGenerator;
import dev.tauri.jsgtransporters.datagen.tags.JSGTFluidTagGenerator;
import dev.tauri.jsgtransporters.datagen.tags.JSGTItemTagGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JSGTransporters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JSGTDataGenerators {
    @SubscribeEvent
    public static void generate(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var exFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();


        generator.addProvider(event.includeServer(), new JSGTRecipeProvider(output));
        //generator.addProvider(event.includeServer(), JSGTLootTableProvider.create(output));
        //generator.addProvider(event.includeServer(), new JSGTWorldgenProvider(output, lookupProvider));

        generator.addProvider(event.includeClient(), new JSGTBlockStateProvider(output, exFileHelper));
        generator.addProvider(event.includeClient(), new JSGTItemModelProvider(output, exFileHelper));

        var blockTagGenerator = generator.addProvider(event.includeServer(), new JSGTBlockTagGenerator(output, lookupProvider, exFileHelper));
        generator.addProvider(event.includeServer(), new JSGTItemTagGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), exFileHelper));

        generator.addProvider(event.includeServer(), new JSGTFluidTagGenerator(output, lookupProvider, exFileHelper));

        generator.addProvider(event.includeClient(), new JSGTLanguageProvider(output, "en_us"));

    }
}
