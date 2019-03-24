package vekta.terrain.visual;

import static vekta.Vekta.v;

public class RoadVisual extends Visual {
	private final float x2, y2;
	private final float width;

	public RoadVisual(float x1, float y1, float x2, float y2, float width) {
		super(x1, y1);
		this.x2 = x2;
		this.y2 = y2;
		this.width = width;
	}

	public float getWidth() {
		return width;
	}

	@Override
	public void draw() {
		v.pushStyle();
		v.strokeWeight(width);
		v.line(getX(), getY(), x2, y2);
		v.popStyle();
	}
}
