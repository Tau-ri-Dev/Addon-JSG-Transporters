package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsg.core.common.registry.helper.TabBuilder;
import dev.tauri.jsg.core.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class JSGTTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER = JSGTransporters.REGISTRY_HELPER.tab();

    public static final RegistryObject<CreativeModeTab> TAB_RINGS = REGISTER.register("rings",
            TabBuilder.create(JSGMapping.rl(JSGTransporters.MOD_ID, "rings"))
                    .withIcon(() -> JSGTBlocks.RINGS_GOAULD).build());

    public static void init() {
    }
}
