package vekta.terrain.visual;

import java.io.Serializable;

public abstract class Visual implements Serializable {
	private final float x, y;

	public Visual(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public abstract void draw();
}
