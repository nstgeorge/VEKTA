package vekta.overlay;

import vekta.Vekta;

public abstract class PositionOverlay implements Overlay {
	private final int x, y;

	public PositionOverlay(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		if(x < 0) {
			return x + Vekta.getInstance().width;
		}
		return x;
	}

	public int getY() {
		if(y < 0) {
			return y + Vekta.getInstance().height;
		}
		return y;
	}
}
