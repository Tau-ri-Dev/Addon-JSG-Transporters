package dev.tauri.jsgtransporters.common.state.gui;

import dev.tauri.jsg.state.State;
import io.netty.buffer.ByteBuf;

public class RingsContainerGuiUpdate extends State {
    public RingsContainerGuiUpdate() {
    }

    public int energyStored;
    public int transferedLastTick;
    public int pageProgress;

    public RingsContainerGuiUpdate(int energyStored, int transferedLastTick, int pageProgress) {
        this.energyStored = energyStored;
        this.transferedLastTick = transferedLastTick;
        this.pageProgress = pageProgress;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(energyStored);
        buf.writeInt(transferedLastTick);
        buf.writeInt(pageProgress);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        energyStored = buf.readInt();
        transferedLastTick = buf.readInt();
        pageProgress = buf.readInt();
    }
}
