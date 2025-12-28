package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsg.api.state.State;
import dev.tauri.jsgtransporters.common.blockentity.rings.RingsAbstractBE;
import io.netty.buffer.ByteBuf;

public class RingsRendererState extends State {
    public long animationStart;
    public boolean isAnimating;
    public int verticalOffset;


    public RingsRendererState() {
    }

    public RingsRendererState(long animationStart, boolean isAnimating) {
        this.animationStart = animationStart;
        this.isAnimating = isAnimating;
    }

    public double getAnimationTick(long ticks, float partialTicks) {
        if (!isAnimating) return 0;
        var tick = (((double) ticks + (double) partialTicks) - (double) animationStart);
        if (tick > RingsAbstractBE.RING_ANIMATION_LENGTH) {
            isAnimating = false;
            return 0;
        }
        return tick;
    }

    public void startAnimation(long time) {
        isAnimating = true;
        animationStart = time;
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAnimating);
        buf.writeLong(animationStart);
        buf.writeInt(verticalOffset);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isAnimating = buf.readBoolean();
        animationStart = buf.readLong();
        verticalOffset = buf.readInt();
    }
}
