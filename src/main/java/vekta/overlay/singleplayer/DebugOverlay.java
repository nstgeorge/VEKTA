package vekta.overlay.singleplayer;

import vekta.profiler.Profiler;
import vekta.profiler.Timing;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;
import static vekta.Vekta.BODY_FONT;
import static vekta.Vekta.v;

public class DebugOverlay implements Overlay {

	private final Profiler profiler;

	public DebugOverlay(Profiler profiler) {
		this.profiler = profiler;
	}

	public Profiler getProfiler() {
		return profiler;
	}

	@Override
	public void render() {
		v.textFont(BODY_FONT);
		v.textAlign(LEFT);
		v.textSize(16);
		v.color(255);

		// FPS
		v.text("FPS = " + v.round(v.frameRate), 50, v.height - 20);

		v.textAlign(RIGHT);
		v.textSize(12);

		// Frame timings
		v.text(generateTimingString(), v.width - 20, 70);
	}

	private String generateTimingString() {
		StringBuilder result = new StringBuilder();

		// Set the last timestamp to the first result (frame start)
		long lastTimestamp = profiler.getTimings().get(0).getTimestamp();

		for(Timing timing : profiler.getTimings()) {
			result.append(timing.getDescriptor()).append(": ").append(timing.getTimestamp() - lastTimestamp).append("ms\n");
			lastTimestamp = timing.getTimestamp();
		}
		return result.toString();
	}
}
