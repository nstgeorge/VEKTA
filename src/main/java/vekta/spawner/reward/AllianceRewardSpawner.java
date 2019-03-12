package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.AllianceReward;
import vekta.spawner.MissionGenerator;

public class AllianceRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3;
	}

	@Override
	public void setup(Mission mission) {
		mission.add(new AllianceReward(mission.getIssuer().getFaction()));
	}
}
