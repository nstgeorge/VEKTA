package vekta.item;

import vekta.Vekta;

import static vekta.Vekta.getInstance;

public enum ItemType {
	COMMON(getInstance().color(220), 1),
	RARE(getInstance().color(255, 200, 100), 5),
	LEGENDARY(getInstance().color(200, 100, 255), 20),
	SPECIAL(getInstance().color(200, 255, 100), 10);

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
		return (int)(Vekta.getInstance().random(1, 10) * getPriceModifier());
	}
}
