package dev.tauri.jsgtransporters.datagen.loot;

import dev.tauri.jsgcore.common.definition.crystal.CrystalType;
import dev.tauri.jsgcore.common.definition.material.MaterialItemType;
import dev.tauri.jsgcore.common.definition.material.MaterialType;
import dev.tauri.jsgcore.datagen.loot.CoreChestLootTables;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class JSGTChestLootTableProvider extends CoreChestLootTables {
    public JSGTChestLootTableProvider(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
        var abydosRingTreasureRoomsChest = ResourceKey.create(Registries.LOOT_TABLE, JSGMapping.rl(JSGTransporters.MOD_ID, "chests/abydos_ring_treasure_rooms"));

        var builder = LootTable.lootTable()
                .setParamSet(LootContextParamSets.CHEST)
                .setRandomSequence(abydosRingTreasureRoomsChest.location())

                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 4.0F))
                        .setBonusRolls(ConstantValue.exactly(0.0F))

                        .add(LootItem.lootTableItem(Items.SAND).setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F), false)))
                )

                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(4.0F, 7.0F))
                        .setBonusRolls(ConstantValue.exactly(0.0F))

                        .add(LootItem.lootTableItem(MaterialType.NAQUADAH.getItem(MaterialItemType.NUGGET).get()).setWeight(7)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F), false)))

                        .add(LootItem.lootTableItem(MaterialType.NAQUADAH.getItem(MaterialItemType.INGOT).get()).setWeight(7)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F), false)))

                        .add(LootItem.lootTableItem(MaterialType.NAQUADAH_ALLOY.getItem(MaterialItemType.NUGGET).get()).setWeight(6)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F), false)))

                        .add(LootItem.lootTableItem(MaterialType.REFINED_NAQUADAH.getItem(MaterialItemType.NUGGET).get()).setWeight(5))

                        .add(LootItem.lootTableItem(CrystalType.YELLOW.getCrystalItem().get()).setWeight(3))
                );

        writer.accept(abydosRingTreasureRoomsChest, builder);
    }
}
