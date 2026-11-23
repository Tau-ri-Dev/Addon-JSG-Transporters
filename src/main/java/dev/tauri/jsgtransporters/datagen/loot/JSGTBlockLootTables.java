package dev.tauri.jsgtransporters.datagen.loot;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.datagen.loot.CoreBlockLootTables;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;
import dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPylon;

public class JSGTBlockLootTables extends CoreBlockLootTables {
    public JSGTBlockLootTables() {
        super();
    }

    @Override
    protected void generate() {
        //TRANSPORT RINGS
        dropAndCopyNBT(JSGTBlocks.RINGS_GOAULD.get(), CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));
        dropAndCopyNBT(JSGTBlocks.RINGS_ORI.get(), CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));
        dropAndCopyNBT(JSGTBlocks.RINGS_ANCIENT.get(), CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));
        
        //TRANSPORT RINGS PANELS
        dropSelf(JSGTBlocks.RINGS_CP_GOAULD.get());
        dropSelf(JSGTBlocks.RINGS_CP_ORI.get());
        dropSelf(JSGTBlocks.RINGS_CP_ANCIENT.get());
        
        // LANTEAN TRANSPORTER
        dropSelf(JSGTBlocks.LANTEAN_TRANSPORTER_ACCESS_PANEL.get());
        dropSelf(JSGTBlocks.LANTEAN_TRANSPORTER_FRAME.get());
        dropSelf(JSGTBlocks.LANTEAN_TRANSPORTER_PLATFORM.get());
        dropSelf(JSGTBlocks.LANTEAN_TRANSPORTER_PANELING.get());
        add(JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get(), LootTable.lootTable()
                .withPool(applyExplosionCondition(JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get(),
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get()))
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(LanteanTransporterPylon.PYLON_PART, 0))))));
        dropSelf(JSGTBlocks.LANTEAN_TRANSPORTER_SCREEN.get());
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return JSGTransporters.REGISTRY_HELPER.block().getEntries().stream().map(RegistryObjectWrapper::get)::iterator;
    }

}
