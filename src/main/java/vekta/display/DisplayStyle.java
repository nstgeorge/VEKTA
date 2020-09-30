package vekta.display;

import java.io.Serializable;

import static vekta.Vekta.v;

public class DisplayStyle implements Serializable, Cloneable {
	private float spacing = 40;
	private float fontSize = 24;
	private int color = v.color(200);
	private DisplayAlign align = DisplayAlign.LEFT;

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
		this.color = v.color(color);
		return this;
	}

	public DisplayAlign align() {
		return align;
	}

	public DisplayStyle align(DisplayAlign align) {
		this.align = align;
		return this;
	}

	public DisplayStyle left() {
		return align(DisplayAlign.LEFT);
	}

	public DisplayStyle right() {
		return align(DisplayAlign.RIGHT);
	}

	public DisplayStyle center() {
		return align(DisplayAlign.CENTER);
	}
}
