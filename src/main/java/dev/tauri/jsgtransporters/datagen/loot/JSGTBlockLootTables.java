package dev.tauri.jsgtransporters.datagen.loot;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.datagen.loot.CoreBlockLootTables;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

public class JSGTBlockLootTables extends CoreBlockLootTables {
    public JSGTBlockLootTables(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        //TRANSPORT RINGS
        dropAndCopyNBT(JSGTBlocks.RINGS_GOAULD.get(), CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));
        dropAndCopyNBT(JSGTBlocks.RINGS_ORI.get(), CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));
        dropAndCopyNBT(JSGTBlocks.RINGS_ANCIENT.get(), CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("itemHandler", "itemHandler").copy("config", "config").copy("energyStorage", "energyStorage"));

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

    protected void dropAndCopyNBT(Block block, CopyCustomDataFunction.Builder copyNbtFunctionBuilder) {
        this.add(block, LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).setBonusRolls(ConstantValue.exactly(0.0F)).add(LootItem.lootTableItem(block).apply(copyNbtFunctionBuilder)))));
    }

}
