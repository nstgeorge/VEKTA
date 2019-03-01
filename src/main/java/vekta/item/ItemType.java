package vekta.item;

import vekta.Vekta;

public enum ItemType {
	COMMON(0xFFCCCCCC, 1),
	RARE(0xFFFFAA88, 5),
	LEGENDARY(0xFFAA88FF, 20);

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
		return (int)(Vekta.getInstance().random(1, 10) * priceModifier);
	}
}
