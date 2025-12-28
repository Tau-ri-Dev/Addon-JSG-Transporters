package dev.tauri.jsgtransporters.common.rings.network;

import dev.tauri.jsg.api.item.NotebookPageSerialization;
import dev.tauri.jsg.api.stargate.network.address.symbol.SymbolUsage;
import dev.tauri.jsgtransporters.JSGTransporters;
import net.minecraft.resources.ResourceLocation;

public class AddressTypeRegistry {

    public static final ResourceLocation RINGS_ADDRESS_TYPE = new ResourceLocation(JSGTransporters.MOD_ID, "rings");
    public static final SymbolUsage RINGS_SYMBOLS = new SymbolUsage("rings");

    public static void register() {
        NotebookPageSerialization.registerDeserializer(RINGS_ADDRESS_TYPE, RingsAddress::new);
    }
}
