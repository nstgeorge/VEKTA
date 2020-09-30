package vekta.person;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;

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

	public OpinionType upgraded() {
		return OpinionType.values()[min(OpinionType.values().length - 1, ordinal() + 1)];
	}

	public OpinionType downgraded() {
		return OpinionType.values()[max(0, ordinal() - 1)];
	}
}
