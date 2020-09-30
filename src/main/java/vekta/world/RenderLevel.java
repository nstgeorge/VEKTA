package vekta.world;

public enum RenderLevel {
	PARTICLE,
	SHIP,
	PLANET,
	STAR,
	INTERSTELLAR;

	public boolean isVisibleTo(RenderLevel dist) {
		return ordinal() >= dist.ordinal();
	}

	public RenderLevel getBelow() {
		if(ordinal() == 0) {
			return null;
		}
		return values()[ordinal() - 1];
	}

	public RenderLevel getAbove() {
		if(ordinal() == values().length - 1) {
			return null;
		}
		return values()[ordinal() + 1];
	}
}
