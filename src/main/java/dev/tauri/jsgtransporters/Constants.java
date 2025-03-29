package dev.tauri.jsgtransporters;

import dev.tauri.jsg.helpers.ItemHelper;
import dev.tauri.jsg.helpers.TabHelper;
import dev.tauri.jsg.loader.LoadersHolder;
import dev.tauri.jsgtransporters.common.registry.ItemRegistry;
import dev.tauri.jsgtransporters.common.registry.TabRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Constants {
    // contains instances of model and texture loaders for this addon
    @OnlyIn(Dist.CLIENT)
    public static final LoadersHolder LOADERS_HOLDER = LoadersHolder.getOrCreate(JSGTransporters.MOD_ID, JSGTransporters.class);

    // helpers
    public static final ItemHelper ITEM_HELPER = new ItemHelper(() -> ItemRegistry.REGISTER);
    public static final TabHelper TAB_HELPER = new TabHelper(() -> TabRegistry.REGISTER, () -> ItemRegistry.REGISTER);


    public static void load() {
    }
}
