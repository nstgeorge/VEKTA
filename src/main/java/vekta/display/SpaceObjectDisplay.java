package vekta.display;

import vekta.object.SpaceObject;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.v;

public class SpaceObjectDisplay implements Display {
	private SpaceObject object;
	private float size;

	public SpaceObjectDisplay(SpaceObject object, float size) {
		this.object = object;
		this.size = size;
	}

	public SpaceObject getObject() {
		return object;
	}

	public void setObject(SpaceObject object) {
		this.object = object;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	@Override
	public float getWidth(float width, float height) {
		return getSize();
	}

	@Override
	public float getHeight(float width, float height) {
		return getSize();
	}

	@Override
	public void draw(float width, float height) {
		if(getObject() != null) {
			v.noFill();
			v.stroke(getObject().getColor());

			// Draw object preview
			v.pushMatrix();
			v.translate(width / 2, height / 4);
			getObject().drawPreview(getSize() / 2);
			v.popMatrix();
		}
		else {
			v.textAlign(CENTER);
			v.textSize(getSize() / 2);
			v.fill(100);

			v.text("?", width / 2, height / 4);
		}
	}
}
