package vekta.overlay;

import static vekta.Vekta.v;

public abstract class TextOverlay extends PositionOverlay {
	public TextOverlay(int x, int y) {
		super(x, y);
	}

	public abstract String getText();

	public abstract int getColor();

	@Override
	public void draw() {
		String text = getText();
		if(text != null) {
			v.fill(v.color(getColor()));
			v.text(text, getX(), getY());
		}
	}
}
