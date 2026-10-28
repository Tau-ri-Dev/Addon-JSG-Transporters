package dev.tauri.jsgtransporters.common.packet.packets;

import dev.tauri.jsgcore.common.packet.NetworkDirection;
import dev.tauri.jsgcore.common.packet.PacketContext;
import dev.tauri.jsgcore.common.packet.packets.PositionedPacket;
import dev.tauri.jsgcore.common.symbol.SymbolInterface;
import dev.tauri.jsgcore.common.symbol.SymbolType;
import dev.tauri.jsgtransporters.common.blockentity.controller.AbstractRingsCPBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CPButtonClickedToServer extends PositionedPacket {
    public CPButtonClickedToServer() {
    }

    public SymbolInterface symbol;
    public SymbolType<?> symbolType;
    public boolean force;

    public CPButtonClickedToServer(BlockPos pos, SymbolInterface symbol) {
        super(pos);
        this.symbol = symbol;
        this.symbolType = symbol.getSymbolType();
    }

    public CPButtonClickedToServer(BlockPos pos, SymbolInterface symbol, boolean force) {
        this(pos, symbol);
        this.force = force;
    }

    public CPButtonClickedToServer(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);

        buf.writeResourceLocation(symbolType.getId());
        buf.writeInt(symbol.getId());
        buf.writeBoolean(force);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        super.fromBytes(buf);

        symbolType = SymbolType.byId(buf.readResourceLocation());
        symbol = symbolType.valueOf(buf.readInt());
        force = buf.readBoolean();
    }

    @Override
    public void handle(PacketContext ctx) {
        if (ctx.getDirection() != NetworkDirection.PLAY_TO_SERVER) return;
        ctx.setPacketHandled(true);
        ServerPlayer player = ctx.getSender();
        if (player == null) return;
        Level world = player.level();
        ctx.enqueueWork(() -> {
            AbstractRingsCPBE be = (AbstractRingsCPBE) world.getBlockEntity(pos);
            if (be == null) return;
            be.pushSymbolButton(symbol, player);
        });
    }
}
