package dev.tauri.jsgtransporters.datagen.tags;

import dev.tauri.jsgcore.datagen.tag.CoreFluidTagGenerator;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTFluidTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

public class JSGTFluidTagGenerator extends CoreFluidTagGenerator {
    public JSGTFluidTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JSGTransporters.MOD_ID, existingFileHelper);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(JSGTFluidTags.TRANSPORTER_FLUIDS).add(Fluids.LAVA);
    }
}
