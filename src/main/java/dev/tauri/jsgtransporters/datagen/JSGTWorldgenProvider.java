package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.datagen.worldgen.JSGTProcessorLists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class JSGTWorldgenProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PROCESSOR_LIST, JSGTProcessorLists::bootstrap);

    public JSGTWorldgenProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, registries, BUILDER, Set.of(JSGTransporters.MOD_ID));
    }
}
