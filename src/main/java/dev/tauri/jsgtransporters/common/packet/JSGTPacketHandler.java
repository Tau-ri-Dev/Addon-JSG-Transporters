package dev.tauri.jsgtransporters.common.packet;

import dev.tauri.jsgcore.common.packet.SimplePacketHandler;
import dev.tauri.jsgcore.common.packet.TargetPoint;
import dev.tauri.jsgcore.mapping.JSGMapping;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import dev.tauri.jsgtransporters.common.packet.packets.SaveRingsSettingsToServer;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class JSGTPacketHandler {

    private static final SimplePacketHandler HANDLER = new SimplePacketHandler(JSGMapping.rl(JSGTransporters.MOD_ID, "main"), "1.0");

    public static void sendToServer(Object packet) {
        HANDLER.sendToServer(packet);
    }

    public static void sendToClient(Object packet, TargetPoint point) {
        HANDLER.sendToClient(packet, point);
    }

    public static void sendTo(Object packet, ServerPlayer player) {
        HANDLER.sendTo(packet, player);
    }

    public static void init() {
        int index = -1;
        // to server
        HANDLER.registerPacketToServer(CPButtonClickedToServer.class);
        HANDLER.registerPacketToServer(SaveRingsSettingsToServer.class);

        // to client
    }
}
