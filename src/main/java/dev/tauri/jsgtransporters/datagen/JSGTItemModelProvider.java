package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.datagen.CoreItemModelProvider;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import dev.tauri.jsgtransporters.common.registry.JSGTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class JSGTItemModelProvider extends CoreItemModelProvider {
    public JSGTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JSGTransporters.MOD_ID, existingFileHelper);
    }

    protected void registerModels() {
        //CRAFTING ELEMENTS
        List.of(JSGTItems.GOAULD_BUTTONS, JSGTItems.ORI_BUTTONS, JSGTItems.ORI_MAIN_BUTTON,
                JSGTItems.ANCIENT_BUTTONS, JSGTItems.FRAGMENT_GOAULD, JSGTItems.FRAGMENT_ORI,
                JSGTItems.FRAGMENT_ANCIENT
        ).forEach(regObj -> {
            var path = regObj.getId().getPath();
            withExistingParent(path, "item/generated")
                    .texture("layer0", JSGMapping.rl(JSGTransporters.MOD_ID, "item/crafting/" + path));
        });

        //UPGRADES
        List.of(JSGTItems.CRYSTAL_GLYPH_GOAULD, JSGTItems.CRYSTAL_GLYPH_ORI, JSGTItems.CRYSTAL_GLYPH_ANCIENT,
                JSGTItems.CRYSTAL_UPGRADE_DIM_TUNNELING
        ).forEach(regObj -> {
            var path = regObj.getId().getPath();
            withExistingParent(path, "item/generated")
                    .texture("layer0", JSGMapping.rl(JSGTransporters.MOD_ID, "item/crystal/" + path));
        });

        //All INVENTORY OBJ MODELS
        List.of(JSGTBlocks.RINGS_CP_GOAULD, JSGTBlocks.RINGS_CP_ORI, JSGTBlocks.RINGS_CP_ANCIENT
        ).forEach(regObj -> {
            var allContext = ItemDisplayContext.values();

            blockOBJModel(regObj, allContext);
        });

    }
}
