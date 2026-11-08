package dev.tauri.jsgtransporters.datagen.tags;

import dev.tauri.jsgcore.datagen.tag.CoreBlockTagGenerator;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

public class JSGTBlockTagGenerator extends CoreBlockTagGenerator {
    public JSGTBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JSGTransporters.MOD_ID, existingFileHelper);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void addTags(HolderLookup.Provider pProvider) {

    }
}
