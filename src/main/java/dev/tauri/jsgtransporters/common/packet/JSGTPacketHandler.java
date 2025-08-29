package dev.tauri.jsgtransporters.common.packet;

import dev.tauri.jsg.api.packet.SimplePacketHandler;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import dev.tauri.jsgtransporters.common.packet.packets.SaveRingsSettingsToServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

@SuppressWarnings("unused")
public class JSGTPacketHandler {

    private static final SimplePacketHandler HANDLER = new SimplePacketHandler(new ResourceLocation(JSGTransporters.MOD_ID, "main"), "1.0");

    public static void sendToServer(Object packet) {
        HANDLER.sendToServer(packet);
    }

    public static void sendToClient(Object packet, PacketDistributor.TargetPoint point) {
        HANDLER.sendToClient(packet, point);
    }

    public static void sendTo(Object packet, ServerPlayer player) {
        HANDLER.sendTo(packet, player);
    }

    public static void init() {
        int index = -1;
        // to server
        HANDLER.registerPacket(CPButtonClickedToServer.class, ++index, NetworkDirection.PLAY_TO_SERVER, CPButtonClickedToServer::new);
        HANDLER.registerPacket(SaveRingsSettingsToServer.class, ++index, NetworkDirection.PLAY_TO_SERVER, SaveRingsSettingsToServer::new);

        // to client
    }
}
