package vekta.display;

import vekta.terrain.visual.Visual;

import static vekta.Vekta.v;

public class VisualDisplay extends StyledDisplay {
	private final Visual visual;
	private float radius;

	public VisualDisplay(Visual visual, float radius) {
		this.visual = visual;
		this.radius = radius;
	}

	public Visual getVisual() {
		return visual;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public float getWidth(float width, float height) {
		return getRadius();
	}

	@Override
	public float getHeight(float width, float height) {
		return getRadius();
	}

	@Override
	public void draw(float width, float height) {
		v.pushMatrix();
		v.translate(width / 2, height / 2);
		v.noFill();//
		v.stroke(getStyle().color());
		v.rotate(v.frameCount / 5000F);
		v.scale(getRadius() / 640); // TODO: improve scaling metric
		getVisual().draw();
		v.popMatrix();
	}
}
