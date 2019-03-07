package vekta.object.particle;

import static vekta.Vekta.v;

public class ColorRange implements ColorSelector {
	private final int a, b;

	public ColorRange(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int selectColor() {
		return v.lerpColor(a, b, v.random(1));
	}
}
