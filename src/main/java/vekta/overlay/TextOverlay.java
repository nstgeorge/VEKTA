package vekta.overlay;

import vekta.Vekta;

public abstract class TextOverlay implements Overlay {
	private final int x, y;

	public TextOverlay(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public abstract String getText();

	public abstract int getColor();

	@Override
	public void draw() {
		String text = getText();
		if(text != null) {
			Vekta v = Vekta.getInstance();
			v.fill(v.color(getColor()));
			int x = getX();
			if(x < 0) {
				x += v.width;
			}
			int y = getY();
			if(y < 0) {
				y += v.height;
			}
			v.text(text, x, y);
		}
	}
}
