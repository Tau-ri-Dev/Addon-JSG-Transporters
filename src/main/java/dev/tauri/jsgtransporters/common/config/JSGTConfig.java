package dev.tauri.jsgtransporters.common.config;

import dev.tauri.jsg.api.config.JSGConfig;
import dev.tauri.jsg.api.config.values.JSGConfigValue;
import dev.tauri.jsg.screen.provider.ConfigScreenClientRegister;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;

public class JSGTConfig {
    private static final String CONFIG_FILE_NAME = "jsg/transporters/";

    private static final ArrayList<JSGConfig.JSGConfigChild> LIST = new ArrayList<>();

    public static final JSGConfig.JSGConfigChild C_GENERAL = new JSGConfig.JSGConfigChild(() -> General.BUILDER, "General", JSGTransporters.MOD_ID);
    public static final JSGConfig.JSGConfigChild C_ENERGY = new JSGConfig.JSGConfigChild(() -> Energy.BUILDER, "Energy", JSGTransporters.MOD_ID);


    public static class General {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final JSGConfigValue.IntValue ringsRange = C_GENERAL.add(new JSGConfigValue.IntValue(BUILDER, "Rings Horizontal Radius", 64, 5, 2048,
                "Rings range radius in same dimension",
                "SIDE: SERVER"
        ));

        public static final JSGConfigValue.IntValue ringsRangeInterDim = C_GENERAL.add(new JSGConfigValue.IntValue(BUILDER, "Rings Dimension Range", 8, 0, 64,
                "Rings range between dimensions.",
                "To setup space between dimensions, use the JSG dimensional config",
                "SIDE: SERVER"
        ));

        public enum FluidTreatmentModes {
            Always,
            Never,
            ByTag,
            ExcludeTag
        }

        public static final JSGConfigValue.EnumValue<FluidTreatmentModes> ringsFluidTreatmentMode = C_GENERAL.add(new JSGConfigValue.EnumValue<>(BUILDER, "Rings fluid treatment mode", FluidTreatmentModes.ExcludeTag,
                "When to affect fluids when transporting them",
                "SIDE: SERVER",
                "\"Always\" always converts source blocks to flowing blocks when transporting them",
                "\"Never\" never converts",
                "\"ByTag\" only converts fluids contained within the jsg_transporters:transporter_fluids tag",
                "\"ExcludeTag\" (Default) converts all fluids except those within the tag"
        ));
    }

    public static class Energy {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final JSGConfigValue.IntValue ringsStartEnergy = C_ENERGY.add(new JSGConfigValue.IntValue(BUILDER, "Rings start power draw", 2048, 0, 500000,
                "SIDE: SERVER"
        ));

        public static final JSGConfigValue.IntValue ringsTransportEnergy = C_ENERGY.add(new JSGConfigValue.IntValue(BUILDER, "Rings entity/block transport power draw", 56, 0, 500000,
                "SIDE: SERVER"
        ));
    }

    public static void register() {
        LIST.clear();
        LIST.add(C_GENERAL);
        LIST.add(C_ENERGY);

        for (JSGConfig.JSGConfigChild child : LIST) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, child.builder.get().build(), CONFIG_FILE_NAME + child.name + ".toml");
        }

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ConfigScreenClientRegister.register(JSGTransporters.MOD_ID, LIST));
    }

    public static void load() {
    }
}
