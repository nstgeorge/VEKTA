package vekta;

public enum RenderLevel {
	AROUND_PARTICLE,
	AROUND_SHIP,
	AROUND_PLANET,
	AROUND_STAR;

	public boolean isVisibleTo(RenderLevel dist) {
		return ordinal() >= dist.ordinal();
	}

	public RenderLevel getBelow() {
		if(ordinal() == 0) {
			return null;
		}
		return values()[Integer.min(0, ordinal() - 1)];
	}

	public RenderLevel getAbove() {
		if(ordinal() == values().length - 1) {
			return null;
		}
		return values()[ordinal() + 1];
	}
}
