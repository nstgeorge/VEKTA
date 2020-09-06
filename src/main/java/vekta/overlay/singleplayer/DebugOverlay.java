package vekta.overlay.singleplayer;

import vekta.benchmarking.FrameTimer;
import vekta.benchmarking.Timing;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static vekta.Vekta.BODY_FONT;
import static vekta.Vekta.v;

public class DebugOverlay implements Overlay {

    private FrameTimer timer;
    private boolean enabled;

    public DebugOverlay(FrameTimer timer) {
        this.timer = timer;
        enabled = false;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render() {
        if(enabled) {
            v.textFont(BODY_FONT);
            v.textAlign(LEFT);
            v.textSize(16);

            // FPS
            v.text("FPS = " + v.round(v.frameRate), 50, v.height - 20);

            v.textAlign(RIGHT);
            v.textSize(12);

            // Frame timings
            v.text(generateTimingString(), v.width - 20, 70);
        }
    }

    private String generateTimingString() {
        String result = "";

        // Set the last timestamp to the first result (frame start)
        long lastTimestamp = timer.getTimings().get(0).getTimestamp();

        for(Timing timing: timer.getTimings()) {
            result += timing.getDescriptor() + ": " + (timing.getTimestamp() - lastTimestamp) + "ms\n";
            lastTimestamp = timing.getTimestamp();
        }
        return result;
    }
}
