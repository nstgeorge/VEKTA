package vekta.mission;

public interface MissionListener {
	default void onStart(Mission mission) {
	}

	default void onComplete(Mission mission) {
	}

	default void onCancel(Mission mission) {
	}
}
