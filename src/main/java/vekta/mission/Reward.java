package vekta.mission;

import vekta.Player;

public abstract class Reward implements MissionListener {

	@Override
	public final void onComplete(Mission mission) {
		for(Player player : mission.getPlayers()) {
			onReward(mission, player);
			player.send("Received: " + getName())
					.withColor(getColor());
		}
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
