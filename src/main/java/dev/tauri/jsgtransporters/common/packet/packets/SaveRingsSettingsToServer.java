package dev.tauri.jsgtransporters.common.packet.packets;

import dev.tauri.jsg.packet.PositionedPacket;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SaveRingsSettingsToServer extends PositionedPacket {
    String name;
    int distance;

    public SaveRingsSettingsToServer(BlockPos pos, String name, int distance) {
        super(pos);
        this.name = name;
        this.distance = distance;
    }

    public SaveRingsSettingsToServer(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(distance);
        buf.writeInt(name.length());
        buf.writeCharSequence(name, StandardCharsets.UTF_8);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        distance = buf.readInt();
        var size = buf.readInt();
        name = buf.readCharSequence(size, StandardCharsets.UTF_8).toString();
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> {
            var entity = Objects.requireNonNull(ctx.getSender()).serverLevel().getBlockEntity(pos);
            if (entity instanceof RingsAbstractBE rings) {
                rings.renameRings(name);
                rings.setVerticalOffset(distance);
            }
        });
    }
}
