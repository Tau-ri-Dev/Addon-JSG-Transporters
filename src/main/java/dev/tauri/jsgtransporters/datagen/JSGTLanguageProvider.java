package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.datagen.CoreLanguageProvider;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import dev.tauri.jsgtransporters.common.registry.JSGTItems;
import dev.tauri.jsgtransporters.common.registry.JSGTSoundEvents;
import net.minecraft.data.PackOutput;

public class JSGTLanguageProvider extends CoreLanguageProvider {
    public JSGTLanguageProvider(PackOutput output, String locate) { super(output, JSGTransporters.MOD_ID, locate);}

    @Override
    protected void addTranslations() {
        //Crafting Elements
        addItemWithCustomName(JSGTItems.GOAULD_BUTTONS, "Goa'uld Controller Buttons");
        addItem(JSGTItems.ORI_BUTTONS);
        addItem(JSGTItems.ORI_MAIN_BUTTON);
        addItem(JSGTItems.ANCIENT_BUTTONS);

        addItemWithCustomName(JSGTItems.FRAGMENT_GOAULD, "Goa'uld Transport Ring Fragment");
        addItem(JSGTItems.FRAGMENT_ORI);
        addItem(JSGTItems.FRAGMENT_ANCIENT);


        //UPGRADES
        addItemWithCustomNameAndTooltip(JSGTItems.CRYSTAL_GLYPH_GOAULD, "Goa'uld Glyph Crystal", "Upgrade for Transport Rings", "This crystal allows you to §lONLY DISPLAY%nl%transport ring's Goauld address when%nl%placed into a rings.%nl%%nl%§oThis upgrade can be combined with other%nl%§otransport rings upgrades!%nl%%nl%This crystal can be placed only in 1st slot!");
        addItemWithTooltip(JSGTItems.CRYSTAL_GLYPH_ORI,  "Upgrade for Transport Rings", "This crystal allows you to §lONLY DISPLAY%nl%transport ring's Ori address when%nl%placed into a rings.%nl%%nl%§oThis upgrade can be combined with other%nl%§otransport rings upgrades!%nl%%nl%This crystal can be placed only in 2nd slot!");
        addItemWithTooltip(JSGTItems.CRYSTAL_GLYPH_ANCIENT,  "Upgrade for Transport Rings", "This crystal allows you to §lONLY  DISPLAY%nl%transport ring's Ancient address when%nl%placed into a rings.%nl%%nl%§oThis upgrade can be combined with other%nl%§otransport rings upgrades!%nl%%nl%This crystal can be placed only in 3rd slot!");
        addItemWithTooltip(JSGTItems.CRYSTAL_UPGRADE_DIM_TUNNELING,  "Upgrade for Transport Rings", "This crystal allows you to §lUSE%nl%transport rings for interdimensional%nl%travel. Only outgoing transport rings%nl%require this crystal!%nl%%nl%§oThis upgrade can be combined%nl%§owith other transport rings upgrades!%nl%%nl%This crystal can be placed only in 4th slot!");

        //BLOCKS
        addBlockWithCustomName(JSGTBlocks.RINGS_GOAULD, "Goa'uld Transport Rings");
        addBlock(JSGTBlocks.RINGS_ORI);
        addBlock(JSGTBlocks.RINGS_ANCIENT);

        addBlockWithCustomNameAndTooltip(JSGTBlocks.RINGS_CP_GOAULD, "Goa'uld Rings Control Panel", "Control panel for Goa'uld transport rings", null);
        addBlockWithTooltip(JSGTBlocks.RINGS_CP_ORI,"Control panel for Ori transport rings",null);
        addBlockWithTooltip(JSGTBlocks.RINGS_CP_ANCIENT,"Control panel for Ancient transport rings",null);

        //GUI
        add("gui." + JSGTransporters.MOD_ID + ".rings_name", "Name");
        add("gui." + JSGTransporters.MOD_ID + ".rings_distance", "Distance");
        add("gui.transportrings.parameters", "Parameters");
        add("gui.transportrings.energy_crystals", "Energy Crystals");

        add("gui.transportrings." + JSGTransporters.MOD_ID + ".goauld_address", "Goa'uld Address");
        add("gui.transportrings." + JSGTransporters.MOD_ID + ".ori_address", "Ori Address");
        add("gui.transportrings." + JSGTransporters.MOD_ID + ".ancient_address", "Ancient Address");

        //ACTION BAR MESSAGES
        add("block." + JSGTransporters.MOD_ID + ".rings.message.address_malformed", "Address malformed");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.out_of_range", "Target Rings are out of range");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.no_power", "Not enough power");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.no_origin", "Address malformed");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.busy", "Rings are busy");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.busy_target", "Target rings are busy");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.obstructed", "Rings are obstructed");
        add("block." + JSGTransporters.MOD_ID + ".rings.message.obstructed_target", "Target rings are obstructed");

        //Config
        add("gui." +  JSGTransporters.MOD_ID + ".config", "JSG Transporters Configuration");
        add("gui." + JSGTransporters.MOD_ID + ".config_child.general", "General");
        add("gui." + JSGTransporters.MOD_ID + ".config_child.energy", "Energy");

        //Subtitles
        addAllSubtitles(JSGTSoundEvents.class);
    }
}
