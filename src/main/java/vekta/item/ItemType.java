package vekta.item;

import vekta.mission.MissionStatus;

import static vekta.Vekta.v;

public enum ItemType {
	COMMON(v.color(220), 1, 2),
	RARE(v.color(255, 200, 100), 5, 2),
	LEGENDARY(v.color(200, 100, 255), 20, 2),
	MODULE(v.color(200, 255, 100), 10, 3),
	MISSION(MissionStatus.READY.getColor(), 2, 10),
	ASSET(v.color(100, 200, 255), 1, 2),
	COLONY(v.color(255, 100, 200), 50, 10);

	private final int color;
	private final float priceModifier;
	private final float markupFactor;

	ItemType(int color, float priceModifier, float markupFactor) {
		this.color = color;
		this.priceModifier = priceModifier;
		this.markupFactor = markupFactor;
	}

	public int getColor() {
		return color;
	}

	public float getPriceModifier() {
		return priceModifier;
	}

	public float getMarkupFactor() {
		return markupFactor;
	}

	public ItemType getImproved() {
		switch(this) {
		case COMMON:
			return RARE;
		case RARE:
			return LEGENDARY;
		default:
			return this;
		}
	}
}
