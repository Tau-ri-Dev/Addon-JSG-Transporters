package dev.tauri.jsgtransporters.datagen.tags;

import dev.tauri.jsgcore.common.registry.tag.CoreBlockTags;
import dev.tauri.jsgcore.datagen.tag.CoreBlockTagGenerator;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import dev.tauri.jsgtransporters.common.registry.tags.JSGTBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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

        //JSG TRANSPORTERS TAGS
        tag(JSGTBlockTags.UNTRANSPORTABLE_BLOCK)
                .addTag(JSGTBlockTags.RINGS_BLOCK)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK)
                //.addTag(JSGTBlockTags.ATLANTIS_TRANSPORTER_BLOCK)
                //.addTag(JSGTBlockTags.OBELISK_BLOCK)
                .addTag(BlockTags.PORTALS)
                .add(
                        Blocks.BARRIER,
                        Blocks.BEDROCK,
                        Blocks.COMMAND_BLOCK,
                        Blocks.CHAIN_COMMAND_BLOCK,
                        Blocks.REPEATING_COMMAND_BLOCK,
                        Blocks.STRUCTURE_BLOCK,
                        Blocks.JIGSAW,
                        Blocks.SPAWNER,
                        Blocks.OBSIDIAN,
                        Blocks.CRYING_OBSIDIAN,
                        Blocks.END_PORTAL_FRAME,
                        Blocks.REINFORCED_DEEPSLATE)
                .addOptionalTag(JSGMapping.rl("jsg", "stargate_any"))
                .addOptionalTag(JSGMapping.rl("jsg", "dhd_any"));

        tag(JSGTBlockTags.UNPENETRABLE_BLOCK)
                .addTag(BlockTags.PORTALS)
                .add(
                        Blocks.BARRIER,
                        Blocks.BEDROCK,
                        Blocks.END_PORTAL_FRAME,
                        Blocks.COMMAND_BLOCK,
                        Blocks.CHAIN_COMMAND_BLOCK,
                        Blocks.REPEATING_COMMAND_BLOCK,
                        Blocks.STRUCTURE_BLOCK,
                        Blocks.JIGSAW
                        );

        //TRANSPORT RINGS
        tag(JSGTBlockTags.RINGS_GOAULD_LINKABLE).add(JSGTBlocks.RINGS_CP_GOAULD.get());
        tag(JSGTBlockTags.RINGS_ORI_LINKABLE).add(JSGTBlocks.RINGS_CP_ORI.get());
        tag(JSGTBlockTags.RINGS_ANCIENT_LINKABLE).add(JSGTBlocks.RINGS_CP_ANCIENT.get());

        tag(JSGTBlockTags.RINGS_BLOCK).add(
                JSGTBlocks.RINGS_GOAULD.get(),
                JSGTBlocks.RINGS_ORI.get(),
                JSGTBlocks.RINGS_ANCIENT.get()
        );

        //TRANSPORT RINGS CONTROLLERS
        tag(JSGTBlockTags.PANEL_GOAULD_LINKABLE).add(JSGTBlocks.RINGS_GOAULD.get());
        tag(JSGTBlockTags.PANEL_ORI_LINKABLE).add(JSGTBlocks.RINGS_ORI.get());
        tag(JSGTBlockTags.PANEL_ANCIENT_LINKABLE).add(JSGTBlocks.RINGS_ANCIENT.get());

        tag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK).add(
                JSGTBlocks.RINGS_CP_GOAULD.get(),
                JSGTBlocks.RINGS_CP_ORI.get(),
                JSGTBlocks.RINGS_CP_ANCIENT.get()
        );

        //CORE TAGS
        tag(CoreBlockTags.BOSS_IMMUNE)
                .addTag(JSGTBlockTags.RINGS_BLOCK)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK
                );


        //MINECRAFT TAGS
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK)
                .addTag(JSGTBlockTags.RINGS_BLOCK);

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK)
                .addTag(JSGTBlockTags.RINGS_BLOCK);

        tag(BlockTags.FEATURES_CANNOT_REPLACE)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK)
                .addTag(JSGTBlockTags.RINGS_BLOCK);

        tag(BlockTags.INVALID_SPAWN_INSIDE)
                .addTag(JSGTBlockTags.RINGS_CONTROLLER_BLOCK)
                .addTag(JSGTBlockTags.RINGS_BLOCK);

    }
}
