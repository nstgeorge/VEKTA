package vekta.item;

import static vekta.Vekta.v;

public enum ItemType {
	COMMON(v.color(220), 1),
	RARE(v.color(255, 200, 100), 5),
	LEGENDARY(v.color(200, 100, 255), 20),
	SPECIAL(v.color(200, 255, 100), 10);

	private final int color;
	private final float priceModifier;

	ItemType(int color, float priceModifier) {
		this.color = color;
		this.priceModifier = priceModifier;
	}

	public int getColor() {
		return color;
	}

	public float getPriceModifier() {
		return priceModifier;
	}

	public int randomPrice() {
		return (int)(v.random(1, 10) * getPriceModifier());
	}
}
