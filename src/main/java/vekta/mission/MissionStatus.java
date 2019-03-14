package vekta.mission;

import static vekta.Vekta.*;

public enum MissionStatus {
	READY(v.color(100, 255, 200)),
	STARTED(v.color(255)),
	CANCELLED(DANGER_COLOR),
	COMPLETED(UI_COLOR);

	private final int color;

	MissionStatus(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	boolean isDone() {
		return this == CANCELLED || this == COMPLETED;
	}
}
