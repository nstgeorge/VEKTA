package vekta.knowledge;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;

public enum ObservationLevel {
	AWARE,
	SCANNED,
	VISITED,
	OWNED;

	public boolean isBetter(ObservationLevel level) {
		return level == null || ordinal() > level.ordinal();
	}

	public boolean isAvailableFrom(ObservationLevel level) {
		return level == this || level.isBetter(this);
	}

	public ObservationLevel increased() {
		return ObservationLevel.values()[min(ObservationLevel.values().length - 1, ordinal() + 1)];
	}

	public ObservationLevel decreased() {
		return ObservationLevel.values()[max(0, ordinal() - 1)];
	}
}
