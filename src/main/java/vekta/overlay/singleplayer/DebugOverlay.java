package vekta.overlay.singleplayer;

import vekta.benchmarking.FrameTimer;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
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

    @Override
    public void render() {
        if(enabled) {
            v.textFont(BODY_FONT);
            v.textAlign(LEFT);
            v.textSize(16);

            // FPS
            v.text("FPS = " + v.round(v.frameRate), 50, v.height - 20);
        }
    }
}
