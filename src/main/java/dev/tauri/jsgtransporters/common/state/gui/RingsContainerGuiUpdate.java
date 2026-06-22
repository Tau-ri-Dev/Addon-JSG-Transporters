package dev.tauri.jsgtransporters.common.state.gui;

import dev.tauri.jsg.core.common.entity.State;
import io.netty.buffer.ByteBuf;

public class RingsContainerGuiUpdate extends State {
    public RingsContainerGuiUpdate() {
    }

    public long energyStored;
    public long transferredLastTick;
    public int pageProgress;

    public RingsContainerGuiUpdate(long energyStored, long transferredLastTick, int pageProgress) {
        this.energyStored = energyStored;
        this.transferredLastTick = transferredLastTick;
        this.pageProgress = pageProgress;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(energyStored);
        buf.writeLong(transferredLastTick);
        buf.writeInt(pageProgress);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        energyStored = buf.readLong();
        transferredLastTick = buf.readLong();
        pageProgress = buf.readInt();
    }
}
