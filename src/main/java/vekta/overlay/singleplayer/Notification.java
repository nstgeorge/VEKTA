package vekta.overlay.singleplayer;

import java.io.Serializable;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class Notification implements Serializable {
	private static final int DEF_COLOR = v.color(200);
	private static final int MAX_AGE = 200;

	private final String message;
	private int color;
	private float timeScale = 1;
	private float progress;

	public Notification(String message) {
		this.message = message;
		this.color = DEF_COLOR;
	}

	public Notification withColor(int color) {
		this.color = color;
		return this;
	}

	public Notification withTime(float scale) {
		this.timeScale = scale;
		return this;
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

	public void remove() {
		progress = 1;
	}

	public void draw(float x, float y) {
		v.fill(v.lerpColor(getColor(), 0, sq(getProgress())));
		v.text(getMessage(), x, y);

		progress += 1F / MAX_AGE / timeScale;
	}
}
