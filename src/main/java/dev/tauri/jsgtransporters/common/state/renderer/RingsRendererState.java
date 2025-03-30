package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.state.State;
import io.netty.buffer.ByteBuf;

public class RingsRendererState extends State {
    public long animationStart;
    public boolean isAnimating;


    public RingsRendererState() {
    }

    public RingsRendererState(long animationStart, boolean isAnimating) {
        this.animationStart = animationStart;
        this.isAnimating = isAnimating;
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAnimating);
        buf.writeLong(animationStart);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isAnimating = buf.readBoolean();
        animationStart = buf.readLong();
    }
}
