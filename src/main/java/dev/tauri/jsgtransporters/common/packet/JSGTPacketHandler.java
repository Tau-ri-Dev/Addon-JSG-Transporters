package dev.tauri.jsgtransporters.common.packet;

import dev.tauri.jsg.packet.JSGPacket;
import dev.tauri.jsgtransporters.JSGTransporters;
import dev.tauri.jsgtransporters.common.packet.packets.CPButtonClickedToServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.Function;

public class JSGTPacketHandler {

    public static void sendToServer(Object packet) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), packet);
    }

    public static void sendToClient(Object packet, PacketDistributor.TargetPoint point) {
        INSTANCE.send(PacketDistributor.NEAR.with(() -> point), packet);
    }

    public static void sendTo(Object packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static final String NETWORK_VERSION = "1.0";

    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(JSGTransporters.MOD_ID, "main"))
            .clientAcceptedVersions((version) -> Objects.equals(version, NETWORK_VERSION))
            .serverAcceptedVersions((version) -> Objects.equals(version, NETWORK_VERSION))
            .networkProtocolVersion(() -> NETWORK_VERSION)
            .simpleChannel();

    public static void init() {
        int index = -1;
        // to server
        registerPacket(CPButtonClickedToServer.class, ++index, NetworkDirection.PLAY_TO_SERVER, CPButtonClickedToServer::new);

        // to client
    }

    public static <MSG extends JSGPacket> void registerPacket(Class<MSG> clazz, int id, NetworkDirection direction, Function<FriendlyByteBuf, MSG> decoder) {
        try {
            INSTANCE.messageBuilder(clazz, id, direction)
                    .encoder(JSGPacket::toBytes)
                    .decoder(decoder)
                    .consumerNetworkThread(JSGPacket::handleSupplier)
                    .add();
        } catch (Exception e) {
            JSGTransporters.logger.error("Could not register packet {}: ", id, e);
        }
    }
}
