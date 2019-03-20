package vekta.observation;

public enum ObservationLevel {
	WITHIN_RANGE,
	TARGETED,
	SCANNED,
	LANDED,
	OWNED;

	public boolean isHigher(ObservationLevel level) {
		return level.ordinal() > ordinal();
	}
}
