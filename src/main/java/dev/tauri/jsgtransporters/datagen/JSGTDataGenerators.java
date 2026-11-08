package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.datagen.*;
import dev.tauri.jsgcore.datagen.loot.CoreLootTableProvider;
import dev.tauri.jsgcore.datagen.tag.*;
import dev.tauri.jsgtransporters.JSGTransporters;
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


        //generator.addProvider(event.includeServer(), new CoreRecipeProvider(output));
        //generator.addProvider(event.includeServer(), CoreLootTableProvider.create(output));
        //generator.addProvider(event.includeServer(), new CoreWorldgenProvider(output, lookupProvider));

        generator.addProvider(event.includeClient(), new JSGTBlockStateProvider(output, exFileHelper));
        generator.addProvider(event.includeClient(), new JSGTItemModelProvider(output, exFileHelper));

        //var blockTagGenerator = generator.addProvider(event.includeServer(), new CoreBlockTagGenerator(output, lookupProvider, exFileHelper));
        //generator.addProvider(event.includeServer(), new CoreItemTagGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), exFileHelper));

        //generator.addProvider(event.includeServer(), new CoreBiomeTagGenerator(output, lookupProvider, exFileHelper));
        //generator.addProvider(event.includeServer(), new CoreFluidTagGenerator(output, lookupProvider, exFileHelper));
        //generator.addProvider(event.includeServer(), new CoreStructureTagGenerator(output, lookupProvider, exFileHelper));
        //generator.addProvider(event.includeServer(), new CorePlacedFeatureTagGenerator(output, lookupProvider, exFileHelper));

        //generator.addProvider(event.includeServer(), CoreAdvancementProvider.create(output, lookupProvider, exFileHelper));

        generator.addProvider(event.includeClient(), new JSGTLanguageProvider(output, "en_us"));

    }
}
