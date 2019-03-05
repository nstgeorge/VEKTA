package vekta.mission;

import vekta.Player;

public interface MissionListener {
	default void onStart(Mission mission, Player player) {
	}

	default void onComplete(Mission mission) {
	}

	default void onCancel(Mission mission) {
	}
}
