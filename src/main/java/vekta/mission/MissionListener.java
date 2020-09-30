package vekta.mission;

import java.io.Serializable;

public interface MissionListener extends Serializable {
	default void onStart(Mission mission) {
	}

	default void onComplete(Mission mission) {
	}

	default void onCancel(Mission mission) {
	}
}
