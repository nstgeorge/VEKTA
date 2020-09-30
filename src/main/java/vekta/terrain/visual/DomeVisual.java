package vekta.terrain.visual;

import static vekta.Vekta.v;

public class DomeVisual extends Visual {
	private final float radius;

	public DomeVisual(float x, float y, float radius) {
		super(x, y);

		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public void draw() {
		v.pushStyle();
		v.strokeWeight(5);
		float r = getRadius();
		v.ellipse(getX(), getY(), r, r);
		v.ellipse(getX(), getY(), r / 2, r / 2);
		v.ellipse(getX(), getY(), r * 2 / 3, r * 2 / 3);
		v.popStyle();
	}
}
