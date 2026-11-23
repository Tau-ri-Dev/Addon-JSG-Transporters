package dev.tauri.jsgtransporters.common.state.renderer;

import dev.tauri.jsgcore.common.entity.State;
import io.netty.buffer.ByteBuf;

public class LanteanTransporterRendererState extends State {
    public long animationStart;
    public boolean isAnimating;

    public LanteanTransporterRendererState() {
    }

    public LanteanTransporterRendererState(long animationStart, boolean isAnimating) {
        this.animationStart = animationStart;
        this.isAnimating = isAnimating;
    }

    public double getAnimationTick(long ticks, float partialTicks) {
        if (!isAnimating) return 0;
        var tick = (((double) ticks + (double) partialTicks) - (double) animationStart);
        if (tick > 20) {
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
    public void fromBytes(ByteBuf buf) {
        isAnimating = buf.readBoolean();
        animationStart = buf.readLong();
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isAnimating);
        buf.writeLong(animationStart);
    }
    
}
