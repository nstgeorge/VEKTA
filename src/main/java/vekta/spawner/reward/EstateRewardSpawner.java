package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.spawner.item.EstateItemSpawner;

public class EstateRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Mission mission) {
		return true;
	}

	@Override
	public void setup(Mission mission) {
		mission.add(new ItemReward(EstateItemSpawner.randomEstateItem(PersonGenerator.randomHome())));
	}
}
