package vekta;

public enum RenderDistance {
	DETAIL,
	NEAR,
	DISTANT;

	public boolean isDetail() {
		return this == DETAIL;
	}

	public boolean isNearby() {
		return this == DETAIL || this == NEAR;
	}
}
