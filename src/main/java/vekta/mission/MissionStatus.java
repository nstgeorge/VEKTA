package vekta.mission;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public enum MissionStatus {
	READY(v.color(100, 255, 200)),
	STARTED(v.color(255)),
	CANCELLED(/*DANGER_COLOR*/100),
	COMPLETED(UI_COLOR);

	private final int color;

	MissionStatus(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public boolean isDone() {
		return this == CANCELLED || this == COMPLETED;
	}
}
