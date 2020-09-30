package vekta.display;

import processing.core.PConstants;

import static processing.core.PApplet.lerp;

// TODO: add vertical variants
public enum DisplayAlign {
	LEFT(0, PConstants.LEFT),
	RIGHT(1, PConstants.RIGHT),
	CENTER(.5F, PConstants.CENTER);

	private final float x;
	private final int alignX;

	DisplayAlign(float x, int alignX) {
		this.x = x;
		this.alignX = alignX;
	}

	public float getX(float min, float max) {
		return lerp(min, max, x);
	}

	public int getAlignCode() {
		return alignX;
	}
}
