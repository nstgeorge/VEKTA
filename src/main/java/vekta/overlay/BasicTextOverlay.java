package vekta.overlay;

import vekta.Vekta;

/**
 * General-purpose overlay with external state management
 */
public class BasicTextOverlay extends TextOverlay {
	private static final int DEF_COLOR = Vekta.getInstance().color(255);

	private String text;
	private int color;

	public BasicTextOverlay(int x, int y) {
		super(x, y);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getColor() {
		return color;
	}

	public void setText(String text) {
		setText(text, DEF_COLOR);
	}

	public void setText(String text, int color) {
		this.text = text;
		this.color = color;
	}

	public void clear() {
		text = null;
	}
}
