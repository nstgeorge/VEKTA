package vekta.overlay;

import static vekta.Vekta.v;

public abstract class PositionOverlay implements Overlay {
	private final int x, y;

	public PositionOverlay(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		if(x < 0) {
			return x + v.width;
		}
		return x;
	}

	public int getY() {
		if(y < 0) {
			return y + v.height;
		}
		return y;
	}
}
