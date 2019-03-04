package vekta.overlay.singleplayer;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

// TODO: convert to abstract class for more notification variants
public class Notification {
	private static final int DEF_COLOR = v.color(200);
	private static final int MAX_AGE = 100;

	private final String message;
	private final int color;
	private float progress;

	public Notification(String message) {
		this(message, DEF_COLOR);
	}

	public Notification(String message, int color) {
		this.message = message;
		this.color = color;
	}

	public String getMessage() {
		return message;
	}

	public int getColor() {
		return color;
	}

	public float getProgress() {
		return progress;
	}

	public boolean isDone() {
		return getProgress() >= 1;
	}

	public void draw(float x, float y) {
		v.fill(v.lerpColor(getColor(), 0, sq(getProgress())));
		v.text(getMessage(), x, y);

		progress += 1F / MAX_AGE;
	}
}
