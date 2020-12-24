package vekta.overlay.singleplayer;

import vekta.overlay.Overlay;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.RIGHT;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class TimeScaleOverlay implements Overlay {
	private static final int PADDING = 15;

	@Override
	public void render() {
		v.textAlign(RIGHT, CENTER);
		v.fill(200);
		int timeScale = Math.round(getWorld().getTimeScale());
		int displayOrder = 1;
		while(timeScale > 100) {
			timeScale /= 10;
			displayOrder *= 10;
		}
		timeScale *= displayOrder;
		v.text(timeScale + "x", v.width - PADDING, PADDING);
	}
}
