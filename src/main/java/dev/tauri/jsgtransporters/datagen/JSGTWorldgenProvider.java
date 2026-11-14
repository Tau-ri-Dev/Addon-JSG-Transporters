package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.datagen.worldgen.JSGTProcessorLists;
import dev.tauri.jsgtransporters.datagen.worldgen.JSGTTemplatePools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class JSGTWorldgenProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PROCESSOR_LIST, JSGTProcessorLists::bootstrap)
            .add(Registries.TEMPLATE_POOL, JSGTTemplatePools::bootstrap);

    public JSGTWorldgenProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries, BUILDER, Set.of(JSGTransporters.MOD_ID));
    }
}
