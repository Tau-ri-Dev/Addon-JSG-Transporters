package dev.tauri.jsgtransporters.datagen.tags;

import dev.tauri.jsgcore.common.registry.CoreItems;
import dev.tauri.jsgcore.datagen.tag.CoreItemTagGenerator;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

public class JSGTItemTagGenerator extends CoreItemTagGenerator {
    public JSGTItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, JSGTransporters.MOD_ID, existingFileHelper);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void addTags(HolderLookup.Provider pProvider) {

        tag(JSGTItemTags.RINGS_ENERGY_CRYSTALS)
                .add(CoreItems.CRYSTAL_ENERGY_BASIC.get())
                .add(CoreItems.CRYSTAL_ENERGY_ADVANCED.get())
                .add(CoreItems.CRYSTAL_ENERGY_ULTIMATE.get())
                .add(CoreItems.CRYSTAL_ENERGY_CREATIVE.get());
    }

}

