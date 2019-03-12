package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.spawner.MissionGenerator;
import vekta.spawner.item.ColonyItemSpawner;

public class ColonyRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 2;
	}

	@Override
	public void setup(Mission mission) {
		mission.add(new ItemReward(ColonyItemSpawner.randomColonyItem()));
	}
}
