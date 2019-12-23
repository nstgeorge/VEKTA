package vekta.display;

import vekta.menu.handle.OceanMenuHandle;

import static vekta.Vekta.v;

public class OceanDisplay implements Display {
	private int color;
	private float height;

	public OceanDisplay(int color, float height) {
		this.color = color;
		this.height = height;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public float getWidth(float width, float height) {
		return width;
	}

	@Override
	public float getHeight(float width, float height) {
		return getHeight();
	}

	@Override
	public void draw(float width, float height) {
		v.stroke(getColor());
		OceanMenuHandle.drawOcean(0, 0, width, height);
	}
}
