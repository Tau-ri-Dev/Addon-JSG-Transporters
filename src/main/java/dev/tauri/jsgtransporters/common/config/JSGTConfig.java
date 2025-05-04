package dev.tauri.jsgtransporters.common.config;

import dev.tauri.jsg.config.JSGConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;

public class JSGTConfig {
    private static final String CONFIG_FILE_NAME = "jsg/transporters/";

    private static final ArrayList<JSGConfig.JSGConfigChild> LIST = new ArrayList<>();

    public static void register() {
        LIST.clear();
        LIST.add(new JSGConfig.JSGConfigChild(General.BUILDER, "General"));
        LIST.add(new JSGConfig.JSGConfigChild(Energy.BUILDER, "Energy"));

        for (JSGConfig.JSGConfigChild child : LIST) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, child.builder.build(), CONFIG_FILE_NAME + child.name + ".toml");
        }
    }

    public static void load() {
    }


    public static class General {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec.IntValue ringsRange = BUILDER
                .comment(
                        "Rings range radius in same dimension",
                        "SIDE: SERVER"
                ).defineInRange("Rings Horizontal Radius", 64, 5, 2048);

        public static final ForgeConfigSpec.IntValue ringsRangeInterDim = BUILDER
                .comment(
                        "Rings range between dimensions.",
                        "To setup space between dimensions, use the JSG dimensional config",
                        "SIDE: SERVER"
                ).defineInRange("Rings Dimension Range", 8, 0, 64);

        public enum FluidTreatmentModes {
            Always,
            Never,
            ByTag,
            ExcludeTag
        }

        public static final ForgeConfigSpec.EnumValue<FluidTreatmentModes> ringsFluidTreatmentMode = BUILDER
                .comment(
                        "When to affect fluids when transporting them",
                        "SIDE: SERVER",
                        "\"Always\" always converts source blocks to flowing blocks when transporting them",
                        "\"Never\" never converts",
                        "\"ByTag\" only converts fluids contained within the jsg_transporters:transporter_fluids tag",
                        "\"ExcludeTag\" (Default) converts all fluids except those within the tag"
                ).defineEnum("Rings fluid treatment mode", FluidTreatmentModes.ExcludeTag);
    }

    public static class Energy {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec.IntValue ringsStartEnergy = BUILDER
                .comment(
                        "SIDE: SERVER"
                ).defineInRange("Rings start power draw", 2048, 0, 500000);

        public static final ForgeConfigSpec.IntValue ringsTransportEnergy = BUILDER
                .comment(
                        "SIDE: SERVER"
                ).defineInRange("Rings entity/block transport power draw", 256, 0, 500000);
    }
}
