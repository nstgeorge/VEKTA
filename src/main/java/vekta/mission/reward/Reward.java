package vekta.mission.reward;

import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.MissionListener;
import vekta.mission.MissionStatus;

public abstract class Reward implements MissionListener {

	@Override
	public final void onComplete(Mission mission) {
		onReward(mission, mission.getPlayer());
		mission.getPlayer().send("Received: " + getName())
				.withColor(getColor())
				.withTime(2);
	}

	public abstract String getName();

	public int getColor() {
		return MissionStatus.COMPLETED.getColor();
	}

	public String getDisplayText() {
		return "Reward: " + getName();
	}

	public void onReward(Mission mission, Player player) {
	}
}
