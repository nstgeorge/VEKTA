package vekta.mission;

import vekta.Player;

public interface MissionListener {
	void onStart(Mission mission, Player player);

	void onComplete(Mission mission);

	void onCancel(Mission mission);
}
