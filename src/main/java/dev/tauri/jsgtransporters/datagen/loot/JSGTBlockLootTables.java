package dev.tauri.jsgtransporters.datagen.loot;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.datagen.loot.CoreBlockLootTables;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class JSGTBlockLootTables extends CoreBlockLootTables {
    public JSGTBlockLootTables() {
        super();
    }

    @Override
    protected void generate() {


        //TRANSPORT RINGS

        //TRANSPORT RINGS PANELS
        dropSelf(JSGTBlocks.RINGS_CP_GOAULD.get());
        dropSelf(JSGTBlocks.RINGS_CP_ORI.get());
        dropSelf(JSGTBlocks.RINGS_CP_ANCIENT.get());
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return JSGTransporters.REGISTRY_HELPER.block().getEntries().stream().map(RegistryObjectWrapper::get)::iterator;
    }

}
