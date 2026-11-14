package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.common.blockstate.JSGProperties;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.datagen.CoreBlockStateProvider;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class JSGTBlockStateProvider extends CoreBlockStateProvider {
    public JSGTBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JSGTransporters.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //TRANSPORT RINGS
        List.of(JSGTBlocks.RINGS_GOAULD, JSGTBlocks.RINGS_ORI, JSGTBlocks.RINGS_ANCIENT
        ).forEach(regObj -> {
            var sideTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_side");
            var topTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_top");
            var bottomTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_bottom");

            buildRingBlock(regObj, topTexture, bottomTexture, sideTexture);
        });

        //CONTROL PANELS
        buildCPBlock(JSGTBlocks.RINGS_CP_GOAULD, JSGMapping.rl("minecraft", "block/gold_block"));
        buildCPBlock(JSGTBlocks.RINGS_CP_ORI, JSGMapping.rl("minecraft", "block/stripped_acacia_log"));
        buildCPBlock(JSGTBlocks.RINGS_CP_ANCIENT, JSGMapping.rl("minecraft", "block/stone"));
    }

    protected void buildRingBlock(RegistryObjectWrapper<? extends Block> blockObj, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation sidesTexture) {
        var modID = blockObj.getId().getNamespace();
        var block = blockObj.get();
        var blockName = blockObj.getId().getPath();

        var model = models().withExistingParent(blockName, "block/cube")
                .texture("particle", sidesTexture)
                .texture("north", sidesTexture)
                .texture("south", sidesTexture)
                .texture("west", sidesTexture)
                .texture("east", sidesTexture)
                .texture("up", topTexture)
                .texture("down", bottomTexture);

        var builder = getVariantBuilder(block);

        for (var facing : JSGProperties.FACING_HORIZONTAL_PROPERTY.getPossibleValues()) {
            var rotationY = switch (facing) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };

            builder.partialState()
                    .with(JSGProperties.FACING_HORIZONTAL_PROPERTY, facing)
                    .addModels(ConfiguredModel.builder().modelFile(model).rotationY(rotationY).build());

            itemModels().withExistingParent(blockName, JSGMapping.rl(modID, "block/" + blockName));
        }
    }

    protected void buildCPBlock(RegistryObjectWrapper<? extends Block> blockObj, ResourceLocation particlePath) {
        var model = models().getBuilder(blockObj.getId().getPath()).texture("particle", particlePath);

        var builder = getVariantBuilder(blockObj.get());

        for (var facing : JSGProperties.FACING_HORIZONTAL_PROPERTY.getPossibleValues()) {
            var rotationY = switch (facing) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };

            builder.partialState()
                    .with(JSGProperties.FACING_HORIZONTAL_PROPERTY, facing)
                    .addModels(ConfiguredModel.builder().modelFile(model).rotationY(rotationY).build());
        }

    }
}
