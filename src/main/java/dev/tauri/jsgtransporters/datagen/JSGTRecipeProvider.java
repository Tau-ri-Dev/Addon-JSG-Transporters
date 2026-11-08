package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.common.definition.crystal.CrystalType;
import dev.tauri.jsgcore.common.definition.material.MaterialType;
import dev.tauri.jsgcore.common.registry.CoreItems;
import dev.tauri.jsgcore.datagen.CoreRecipeProvider;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import dev.tauri.jsgtransporters.common.registry.JSGTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

public class JSGTRecipeProvider extends CoreRecipeProvider {
    public JSGTRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //CRAFTING ELEMENTS
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.GOAULD_BUTTONS.get())
                .group(JSGTransporters.MOD_ID + ":goauld_buttons")
                .pattern("EGE")
                .pattern("ECE")
                .pattern("EGE")
                .define('E', Tags.Items.GEMS_EMERALD)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.ORI_BUTTONS.get())
                .group(JSGTransporters.MOD_ID + ":ori_buttons")
                .pattern("ICI")
                .pattern("GGG")
                .pattern("GGG")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('G', CrystalType.BLUE.forgeTags().gemsSmall())
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_small_blue_crystal", has(CrystalType.BLUE.forgeTags().gemsSmall()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.ORI_MAIN_BUTTON.get())
                .group(JSGTransporters.MOD_ID + ":ori_buttons")
                .pattern(" L ")
                .pattern("LSL")
                .pattern(" L ")
                .define('L', CrystalType.BLUE.forgeTags().gems())
                .define('S', CrystalType.BLUE.forgeTags().gemsSmall())
                .unlockedBy("has_small_blue_crystal", has(CrystalType.BLUE.forgeTags().gemsSmall()))
                .unlockedBy("has_blue_crystal", has(CrystalType.BLUE.forgeTags().gems()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.ANCIENT_BUTTONS.get())
                .group(JSGTransporters.MOD_ID + ":ancient_buttons")
                .pattern("BCB")
                .pattern("SBS")
                .pattern("BBB")
                .define('B', ItemTags.STONE_BUTTONS)
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('S', Tags.Items.STONE)
                .unlockedBy("has_button", has(ItemTags.STONE_BUTTONS))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_stone", has(Tags.Items.STONE))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.FRAGMENT_GOAULD.get())
                .group(JSGTransporters.MOD_ID + ":rings_fragment")
                .pattern("PIP")
                .pattern("PIP")
                .pattern("PIP")
                .define('P', MaterialType.NAQUADAH_ALLOY.forgeTags().plates())
                .define('I', MaterialType.TITANIUM.forgeTags().ingots())
                .unlockedBy("has_naquadah_alloy_plate", has(MaterialType.NAQUADAH_ALLOY.forgeTags().plates()))
                .unlockedBy("has_titanium_ingot", has(MaterialType.TITANIUM.forgeTags().ingots()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.FRAGMENT_ORI.get())
                .group(JSGTransporters.MOD_ID + ":rings_fragment")
                .pattern("PIP")
                .pattern("PIP")
                .pattern("PIP")
                .define('P', MaterialType.TRINIUM.forgeTags().plates())
                .define('I', MaterialType.NAQUADAH_ALLOY.forgeTags().ingots())
                .unlockedBy("has_trinium_plate", has(MaterialType.TRINIUM.forgeTags().plates()))
                .unlockedBy("has_naquadah_alloy_ingot", has(MaterialType.NAQUADAH_ALLOY.forgeTags().ingots()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.FRAGMENT_ANCIENT.get())
                .group(JSGTransporters.MOD_ID + ":rings_fragment")
                .pattern("PIP")
                .pattern("PIP")
                .pattern("PIP")
                .define('P', MaterialType.NAQUADAH_ALLOY.forgeTags().plates())
                .define('I', MaterialType.NAQUADAH_ALLOY.forgeTags().ingots())
                .unlockedBy("has_naquadah_alloy_plate", has(MaterialType.NAQUADAH_ALLOY.forgeTags().plates()))
                .unlockedBy("has_naquadah_alloy_ingot", has(MaterialType.NAQUADAH_ALLOY.forgeTags().ingots()))
                .save(pWriter);

        //CRYSTALS
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.CRYSTAL_GLYPH_GOAULD.get())
                .group(JSGTransporters.MOD_ID + ":glyph_crystals")
                .pattern("REB")
                .pattern("RYB")
                .pattern("III")
                .define('R', CrystalType.RED.forgeTags().gems())
                .define('E', CrystalType.ENDER.forgeTags().gems())
                .define('B', CrystalType.BLUE.forgeTags().gems())
                .define('Y', CrystalType.YELLOW.forgeTags().gems())
                .define('I', MaterialType.TITANIUM.forgeTags().ingots())
                .unlockedBy("has_red_crystal", has(CrystalType.RED.forgeTags().gems()))
                .unlockedBy("has_ender_crystal", has(CrystalType.ENDER.forgeTags().gems()))
                .unlockedBy("has_blue_crystal", has(CrystalType.BLUE.forgeTags().gems()))
                .unlockedBy("has_yellow_crystal", has(CrystalType.YELLOW.forgeTags().gems()))
                .unlockedBy("has_titanium_ingot", has(MaterialType.NAQUADAH_ALLOY.forgeTags().ingots()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.CRYSTAL_GLYPH_ORI.get())
                .group(JSGTransporters.MOD_ID + ":glyph_crystals")
                .pattern("WPW")
                .pattern("GGG")
                .pattern("WPW")
                .define('W', Tags.Items.GLASS_PANES_WHITE)
                .define('P', Tags.Items.GLASS_PANES_PURPLE)
                .define('G', CrystalType.WHITE.forgeTags().gemsSmall())
                .unlockedBy("has_white_pane", has(Tags.Items.GLASS_PANES_WHITE))
                .unlockedBy("has_purple_pane", has(Tags.Items.GLASS_PANES_PURPLE))
                .unlockedBy("has_white_small_crystal", has(CrystalType.WHITE.forgeTags().gemsSmall()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.CRYSTAL_GLYPH_ANCIENT.get())
                .group(JSGTransporters.MOD_ID + ":glyph_crystals")
                .pattern("W W")
                .pattern("GWG")
                .pattern("WBW")
                .define('W', Tags.Items.GLASS_PANES_WHITE)
                .define('B', Tags.Items.GLASS_PANES_LIGHT_BLUE)
                .define('G', CrystalType.BLUE.forgeTags().gemsSmall())
                .unlockedBy("has_white_pane", has(Tags.Items.GLASS_PANES_WHITE))
                .unlockedBy("has_light_blue_pane", has(Tags.Items.GLASS_PANES_LIGHT_BLUE))
                .unlockedBy("has_blue_small_crystal", has(CrystalType.BLUE.forgeTags().gemsSmall()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, JSGTItems.CRYSTAL_UPGRADE_DIM_TUNNELING.get())
                .group(JSGTransporters.MOD_ID + ":" + JSGTItems.CRYSTAL_UPGRADE_DIM_TUNNELING.getId().getPath())
                .pattern("G G")
                .pattern("NPN")
                .pattern("IGI")
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('N', Blocks.CHISELED_NETHER_BRICKS)
                .define('P', Blocks.PURPUR_BLOCK)
                .define('I', Tags.Items.INGOTS_NETHERITE)
                .unlockedBy("has_pane", has(Tags.Items.GLASS_PANES_COLORLESS))
                .unlockedBy("has_nether_bricks", has(Blocks.CHISELED_NETHER_BRICKS))
                .unlockedBy("has_purpur_block", has(Blocks.PURPUR_BLOCK))
                .unlockedBy("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE))
                .save(pWriter);

        //TRANSPORT RINGS
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_GOAULD.get())
                .group(JSGTransporters.MOD_ID + ":goauld_rings")
                .pattern("CFN")
                .pattern("FAF")
                .pattern("RFR")
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('F', JSGTItems.FRAGMENT_GOAULD.get())
                .define('N', CoreItems.CIRCUIT_CONTROL_NAQUADAH.get())
                .define('A', CoreItems.CRYSTAL_ENERGY_BASIC.get())
                .define('R', Blocks.COMPARATOR)
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_ring_fragment", has(JSGTItems.FRAGMENT_GOAULD.get()))
                .unlockedBy("has_control_naquadah", has(CoreItems.CIRCUIT_CONTROL_NAQUADAH.get()))
                .unlockedBy("has_comparator", has(Blocks.COMPARATOR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_ORI.get())
                .group(JSGTransporters.MOD_ID + ":ori_rings")
                .pattern("CFN")
                .pattern("FAF")
                .pattern("RFR")
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('F', JSGTItems.FRAGMENT_ORI.get())
                .define('N', CoreItems.CIRCUIT_CONTROL_NAQUADAH.get())
                .define('A', CoreItems.CRYSTAL_ENERGY_BASIC.get())
                .define('R', Blocks.COMPARATOR)
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_ring_fragment", has(JSGTItems.FRAGMENT_ORI.get()))
                .unlockedBy("has_control_naquadah", has(CoreItems.CIRCUIT_CONTROL_NAQUADAH.get()))
                .unlockedBy("has_comparator", has(Blocks.COMPARATOR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_ANCIENT.get())
                .group(JSGTransporters.MOD_ID + ":ancients_rings")
                .pattern("CFN")
                .pattern("FAF")
                .pattern("RFR")
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('F', JSGTItems.FRAGMENT_ANCIENT.get())
                .define('N', CoreItems.CIRCUIT_CONTROL_NAQUADAH.get())
                .define('A', CoreItems.CRYSTAL_ENERGY_BASIC.get())
                .define('R', Blocks.COMPARATOR)
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_ring_fragment", has(JSGTItems.FRAGMENT_ANCIENT.get()))
                .unlockedBy("has_control_naquadah", has(CoreItems.CIRCUIT_CONTROL_NAQUADAH.get()))
                .unlockedBy("has_comparator", has(Blocks.COMPARATOR))
                .save(pWriter);

        //TRANSPORT RINGS CONTROL PANELS
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_CP_GOAULD.get())
                .group(JSGTransporters.MOD_ID + ":goauld_rings")
                .pattern("GOG")
                .pattern("LBL")
                .pattern("GCG")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('O', Tags.Items.INGOTS_COPPER)
                .define('L', Blocks.REDSTONE_LAMP)
                .define('B', JSGTItems.GOAULD_BUTTONS.get())
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .unlockedBy("has_redstone_lamp", has(Blocks.REDSTONE_LAMP))
                .unlockedBy("has_buttons", has(JSGTItems.GOAULD_BUTTONS.get()))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_CP_ORI.get())
                .group(JSGTransporters.MOD_ID + ":ori_rings")
                .pattern("III")
                .pattern("BWB")
                .pattern("CMN")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('B', JSGTItems.ORI_BUTTONS.get())
                .define('W', Ingredient.of(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG))
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .define('M', JSGTItems.ORI_MAIN_BUTTON.get())
                .define('N', CoreItems.CIRCUIT_CONTROL_NAQUADAH.get())
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_buttons", has(JSGTItems.ORI_BUTTONS.get()))
                .unlockedBy("has_wood", has(ItemTags.LOGS))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .unlockedBy("has_main_button", has(JSGTItems.ORI_MAIN_BUTTON.get()))
                .unlockedBy("has_control_naquadah", has(CoreItems.CIRCUIT_CONTROL_NAQUADAH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, JSGTBlocks.RINGS_CP_ANCIENT.get())
                .group(JSGTransporters.MOD_ID + ":ancients_rings")
                .pattern("SSS")
                .pattern("SBS")
                .pattern("NGC")
                .define('S', Tags.Items.STONE)
                .define('B', JSGTItems.ANCIENT_BUTTONS.get())
                .define('N', CoreItems.CIRCUIT_CONTROL_NAQUADAH.get())
                .define('G', CrystalType.WHITE.forgeTags().gemsSmall())
                .define('C', CoreItems.CIRCUIT_CONTROL_CRYSTAL.get())
                .unlockedBy("has_stone", has(Tags.Items.STONE))
                .unlockedBy("has_buttons", has(JSGTItems.ANCIENT_BUTTONS.get()))
                .unlockedBy("has_control_naquadah", has(CoreItems.CIRCUIT_CONTROL_NAQUADAH.get()))
                .unlockedBy("has_small_white_crystal", has(CrystalType.WHITE.forgeTags().gemsSmall()))
                .unlockedBy("has_control_crystal", has(CoreItems.CIRCUIT_CONTROL_CRYSTAL.get()))
                .save(pWriter);
    }
}
