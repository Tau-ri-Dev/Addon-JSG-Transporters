package dev.tauri.jsgtransporters.common.registry;

import dev.tauri.jsgcore.common.registry.helper.DeferredRegisterWrapper;
import dev.tauri.jsgcore.common.registry.helper.RegistryObjectWrapper;
import dev.tauri.jsgcore.common.registry.helper.TabBuilder;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.world.item.CreativeModeTab;

public class JSGTTabs {
    private static final DeferredRegisterWrapper<CreativeModeTab> REGISTER = JSGTransporters.REGISTRY_HELPER.tab();

    public static final RegistryObjectWrapper<CreativeModeTab> TAB_RINGS = REGISTER.register("rings",
            TabBuilder.create(JSGMapping.rl(JSGTransporters.MOD_ID, "rings"))
                    .withIcon(() -> JSGTBlocks.RINGS_GOAULD).build());

    public static void init() {
    }
}
