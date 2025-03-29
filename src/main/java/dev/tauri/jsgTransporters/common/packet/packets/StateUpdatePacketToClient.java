package dev.tauri.jsgTransporters.common.packet.packets;

import dev.tauri.jsg.packet.PositionedPacket;
import dev.tauri.jsg.state.State;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.NotImplementedException;
import dev.tauri.jsgTransporters.common.state.StateType;
import dev.tauri.jsgTransporters.JSGTransporters;
import dev.tauri.jsgTransporters.common.state.StateProvider;

public class StateUpdatePacketToClient extends PositionedPacket {
    private StateType stateType;
    private State state;

    private ByteBuf stateBuf;

    public StateUpdatePacketToClient(BlockPos pos, StateType stateType, State state) {
        super(pos);

        this.stateType = stateType;
        if (state == null) {
            throw new NullPointerException("State was null! (State type: " + stateType.toString() + "; Pos: " + pos.toString() + ")");
        }

        this.state = state;
    }

    public StateUpdatePacketToClient(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(stateType.id);
        state.toBytes(buf);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        stateType = StateType.byId(buf.readInt());
        stateBuf = buf.copy();
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        if (ctx.getDirection() != NetworkDirection.PLAY_TO_CLIENT) return;
        ctx.setPacketHandled(true);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ClientLevel level = player.clientLevel;
        ctx.enqueueWork(() -> {
            StateProvider te = (StateProvider) level.getBlockEntity(pos);
            try {
                if (te == null)
                    return;

                State state = te.createState(stateType);

                if (state != null) {
                    state.fromBytes(stateBuf);

                    te.setState(stateType, state);
                } else {
                    throw new NotImplementedException("State not implemented on " + te.getClass().getCanonicalName());
                }
            } catch (Exception e) {
                JSGTransporters.logger.error("Error while handling packet!", e);
            }
        });
    }
}
