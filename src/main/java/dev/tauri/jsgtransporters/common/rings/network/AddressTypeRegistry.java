package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.api.item.NotebookPageSerialization;
import dev.tauri.jsg.stargate.network.SymbolUsage;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

public class AddressTypeRegistry {

    public static ResourceLocation RINGS_ADDRESS_TYPE = new ResourceLocation(JSGTransporters.MOD_ID, "rings");
    public static SymbolUsage RINGS_SYMBOLS = new SymbolUsage("rings");

    public static void register() {
        NotebookPageSerialization.registerDeserializer(RINGS_ADDRESS_TYPE, RingsAddress::new);
    }
}
