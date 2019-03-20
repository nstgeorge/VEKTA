package vekta.display;

import java.io.Serializable;

import static vekta.Vekta.v;

public class DisplayStyle implements Serializable, Cloneable {
	private float spacing = 40;
	private float fontSize = 24;
	private int color = v.color(200);

	public DisplayStyle() {
	}

	public DisplayStyle derive() {
		try {
			return (DisplayStyle)clone();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public float spacing() {
		return spacing;
	}

	public DisplayStyle spacing(float spacing) {
		this.spacing = spacing;
		return this;
	}

	public float fontSize() {
		return fontSize;
	}

	public DisplayStyle fontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public int color() {
		return color;
	}

	public DisplayStyle color(int color) {
		this.color = color;
		return this;
	}
}
