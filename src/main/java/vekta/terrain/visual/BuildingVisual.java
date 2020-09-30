package vekta.terrain.visual;

import static processing.core.PApplet.*;
import static processing.core.PConstants.QUAD_STRIP;
import static vekta.Vekta.v;

public class BuildingVisual extends Visual {
	private final float x2, y2;
	private final float size;
	private final float ratio;

	public BuildingVisual(float x1, float y1, float x2, float y2, float size, float ratio) {
		super(x1, y1);
		this.x2 = x2;
		this.y2 = y2;
		this.size = size;
		this.ratio = ratio;
	}

	public float getSize() {
		return size;
	}

	public float getRatio() {
		return ratio;
	}

	@Override
	public void draw() {
		float dx = x2 - getX();
		float dy = y2 - getY();
		float dist = sqrt(sq(dx) + sq(dy));

		v.pushMatrix();
		v.translate(getX(), getY());
		v.rotate(atan2(dy, dx));

		v.pushStyle();
		v.strokeWeight(max(1, size / 10));

		v.beginShape(QUAD_STRIP);
		for(float f = 0; f <= dist; f += getSize() * getRatio()) {
			v.vertex(f, -size);
			v.vertex(f, size);
		}
		v.endShape();

		v.popStyle();
		v.popMatrix();
	}
}
