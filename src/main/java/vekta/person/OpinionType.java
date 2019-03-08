package vekta.person;

public enum OpinionType {
	ENEMY,
	UNFRIENDLY,
	NEUTRAL,
	FRIENDLY,
	GRATEFUL;

	public boolean isPositive() {
		return ordinal() > NEUTRAL.ordinal();
	}

	public boolean isNegative() {
		return ordinal() < NEUTRAL.ordinal();
	}
}
