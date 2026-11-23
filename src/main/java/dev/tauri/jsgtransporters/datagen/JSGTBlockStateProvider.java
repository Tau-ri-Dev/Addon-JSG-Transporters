package dev.tauri.jsgtransporters.datagen;

import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.datagen.CoreBlockStateProvider;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.registry.JSGTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import java.util.List;
import java.util.Map;

public class JSGTBlockStateProvider extends CoreBlockStateProvider {
    public JSGTBlockStateProvider (PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JSGTransporters.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //TRANSPORT RINGS
        List.of(JSGTBlocks.RINGS_GOAULD, JSGTBlocks.RINGS_ORI, JSGTBlocks.RINGS_ANCIENT
        ).forEach(regObj -> {
            var sideTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_side");
            var topTexture =  JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_top");
            var bottomTexture =  JSGMapping.rl(JSGTransporters.MOD_ID, "block/rings/" + regObj.getId().getPath() + "_bottom");

            buildRingBlock(regObj, topTexture, bottomTexture, sideTexture);
        });

        //CONTROL PANELS
        buildCPBlock(JSGTBlocks.RINGS_CP_GOAULD, JSGMapping.rl("minecraft", "block/gold_block"));
        buildCPBlock(JSGTBlocks.RINGS_CP_ORI, JSGMapping.rl("minecraft", "block/stripped_acacia_log"));
        buildCPBlock(JSGTBlocks.RINGS_CP_ANCIENT, JSGMapping.rl("minecraft", "block/stone"));

        // LANTEAN TRANSPORTER FRAME
        buildLanteanTransporterFrame();
        buildLanteanTransporterPlatform();
        buildLanteanTransporterPylon();
    }

    protected void buildRingBlock(RegistryObjectWrapper<? extends Block> blockObj, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation sidesTexture) {
        var modID = blockObj.getId().getNamespace();
        var block = blockObj.get();
        var blockName = blockObj.getId().getPath();

        BlockModelBuilder model = models().withExistingParent(blockName, "block/cube")
                .texture("particle", sidesTexture)
                .texture("north", sidesTexture)
                .texture("south", sidesTexture)
                .texture("west", sidesTexture)
                .texture("east", sidesTexture)
                .texture("up", topTexture)
                .texture("down", bottomTexture);

        VariantBlockStateBuilder builder = getVariantBuilder(block);

        Map<Direction, Integer> yRotations = Map.of(
                Direction.NORTH, 0,
                Direction.EAST, 90,
                Direction.SOUTH, 180,
                Direction.WEST, 270
        );

        yRotations.forEach((direction, yRot) -> builder.partialState()
                .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                .setModels(new ConfiguredModel(model, 0, yRot, false)));

        itemModels().withExistingParent(blockName, JSGMapping.rl(modID, "block/" + blockName));
    }
    protected void buildCPBlock(RegistryObjectWrapper<? extends Block> blockObj, ResourceLocation particlePath) {
        var model = models().getBuilder(blockObj.getId().getPath()).texture("particle", particlePath);

        VariantBlockStateBuilder builder = getVariantBuilder(blockObj.get());

        Map<Direction, Integer> yRotations = Map.of(
                Direction.NORTH, 0,
                Direction.EAST, 90,
                Direction.SOUTH, 180,
                Direction.WEST, 270
        );

        yRotations.forEach((direction, yRot) -> builder.partialState()
                .with(BlockStateProperties.HORIZONTAL_FACING, direction)
                .setModels(new ConfiguredModel(model, 0, yRot, false)));

    }

    public static final ResourceLocation platingTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/lantean_transporter/frame"); //TODO: Change to plating texture when available
    protected void buildLanteanTransporterPlatform() {
        var blockName = JSGTBlocks.LANTEAN_TRANSPORTER_PLATFORM.getId().getPath();
        ResourceLocation platformTexture = JSGMapping.rl(JSGTransporters.MOD_ID, "block/lantean_transporter/frame"); //TODO: Change to platform texture when available
        BlockModelBuilder model = models().withExistingParent(blockName, "block/cube")
                .texture("particle", platingTexture)
                .texture("north", platingTexture)
                .texture("south", platingTexture)
                .texture("west", platingTexture)
                .texture("east", platingTexture)
                .texture("up", platformTexture)
                .texture("down", platingTexture);

        getVariantBuilder(JSGTBlocks.LANTEAN_TRANSPORTER_PLATFORM.get())
                .partialState()
                .setModels(new ConfiguredModel(model));

        itemModels().withExistingParent(blockName,
                JSGMapping.rl(JSGTransporters.MOD_ID, "block/" + blockName));
    }

    protected void buildLanteanTransporterPylon() {
        var block = JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.get();
        var lower = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/pylon_lower"));
        var middle = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/pylon_middle"));
        var upper = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/pylon_upper"));
        var multipart = getMultipartBuilder(block);

        multipart.part().modelFile(lower).addModel()
                .condition(dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPylon.PYLON_PART, 0).end();
        multipart.part().modelFile(middle).addModel()
                .condition(dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPylon.PYLON_PART, 1).end();
        multipart.part().modelFile(upper).addModel()
                .condition(dev.tauri.jsgtransporters.common.block.lantean_transporter.LanteanTransporterPylon.PYLON_PART, 2).end();

        itemModels().withExistingParent(
                JSGTBlocks.LANTEAN_TRANSPORTER_PYLON.getId().getPath(),
                JSGMapping.rl(JSGTransporters.MOD_ID, "block/lantean_transporter/pylon_lower"));
    }

    protected void buildLanteanTransporterFrame() {
        var block = JSGTBlocks.LANTEAN_TRANSPORTER_FRAME.get();
        var blockName = JSGTBlocks.LANTEAN_TRANSPORTER_FRAME.getId().getPath();

        var hub = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_hub"));
        var up = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_up"));
        var down = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_down"));
        var north = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_north"));
        var south = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_south"));
        var east = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_east"));
        var west = models().getExistingFile(JSGMapping.rl(JSGTransporters.MOD_ID,
                "block/lantean_transporter/frame_west"));

        var multipart = getMultipartBuilder(block);
        multipart.part().modelFile(hub).addModel().end();
        multipart.part().modelFile(up).addModel().condition(BlockStateProperties.UP, true).end();
        multipart.part().modelFile(down).addModel().condition(BlockStateProperties.DOWN, true).end();
        multipart.part().modelFile(north).addModel().condition(BlockStateProperties.NORTH, true).end();
        multipart.part().modelFile(south).addModel().condition(BlockStateProperties.SOUTH, true).end();
        multipart.part().modelFile(east).addModel().condition(BlockStateProperties.EAST, true).end();
        multipart.part().modelFile(west).addModel().condition(BlockStateProperties.WEST, true).end();

        itemModels().withExistingParent(blockName,
                JSGMapping.rl(JSGTransporters.MOD_ID, "block/lantean_transporter/frame_hub"));
    }
}
