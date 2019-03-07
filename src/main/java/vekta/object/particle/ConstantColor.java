package vekta.object.particle;

import static vekta.Vekta.v;

public class ConstantColor implements ColorSelector {
	private final int color;

	public ConstantColor(int color) {
		this.color = color;
	}

	@Override
	public int selectColor() {
		return v.color(color);
	}
}
