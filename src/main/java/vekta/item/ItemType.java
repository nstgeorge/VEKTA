package vekta.item;

import vekta.mission.MissionStatus;

import static vekta.Vekta.v;

public enum ItemType {
	COMMON(v.color(220), 1, 1.5F),
	RARE(v.color(255, 200, 100), 5, 1.5F),
	LEGENDARY(v.color(200, 100, 255), 20, 1.5F),
	MODULE(v.color(200, 255, 100), 10, 3F),
	MISSION(MissionStatus.READY.getColor(), 10, 10F),
	COLONY(v.color(255, 100, 200), 50, 10F);

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
