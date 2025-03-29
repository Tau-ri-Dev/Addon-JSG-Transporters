package dev.tauri.jsgTransporters.common.packet.packets;

import dev.tauri.jsg.packet.JSGPacketHandler;
import dev.tauri.jsg.packet.PositionedPacket;
import dev.tauri.jsg.state.State;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.NotImplementedException;

import dev.tauri.jsgTransporters.JSGTransporters;
import dev.tauri.jsgTransporters.common.state.StateProvider;
import dev.tauri.jsgTransporters.common.state.StateType;

public class StateUpdateRequestToServer extends PositionedPacket {
    StateType stateType;

    public StateUpdateRequestToServer(BlockPos pos, StateType stateType) {
        super(pos);
        this.stateType = stateType;
    }

    public StateUpdateRequestToServer(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);

        buf.writeInt(stateType.id);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);
        stateType = StateType.byId(buf.readInt());
    }

    @Override
    public void handle(NetworkEvent.Context ctx) {
        if (ctx.getDirection() != NetworkDirection.PLAY_TO_SERVER) return;
        ctx.setPacketHandled(true);
        ServerPlayer player = ctx.getSender();
        if (player != null) {
            ServerLevel level = player.serverLevel();
            ctx.enqueueWork(() -> {
                StateProvider te = (StateProvider) level.getBlockEntity(pos);

                if (te != null) {
                    try {
                        State state = te.getState(stateType);

                        if (state != null)
                            JSGPacketHandler.sendTo(new StateUpdatePacketToClient(pos, stateType, state), player);
                        else
                            throw new NotImplementedException("State not implemented on " + te.getClass().getCanonicalName() + " : " + stateType.toString());
                    } catch (Exception e) {
                        JSGTransporters.logger.error("Error while handling packet!", e);
                    }
                }
            });
        }
    }
}
