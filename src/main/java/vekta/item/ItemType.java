package vekta.item;

import vekta.mission.MissionStatus;

import static vekta.Vekta.v;

public enum ItemType {
	JUNK(v.color(150, 200, 175), .1F, 1),
	COMMON(v.color(220), 1, 1),
	RARE(v.color(255, 200, 100), 5, 2),
	LEGENDARY(v.color(200, 100, 255), 20, 2),
	KNOWLEDGE(v.color(130, 140, 255), 1, 2),
	DANGEROUS(v.color(255, 100, 200), 4, 2),
	MODULE(v.color(200, 255, 100), 10, 3),
	RECIPE(v.color(100, 255, 200), 20, 20),
	MISSION(MissionStatus.READY.getColor(), 2, 5),
	ECONOMY(v.color(100, 200, 255), 1, 2),
	ECOSYSTEM(v.color(120, 200, 100), 1, 2),
	COLONY(v.color(170, 80, 220), 50, 10);

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
