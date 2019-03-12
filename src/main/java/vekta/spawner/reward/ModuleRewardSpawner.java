package vekta.spawner.reward;

import vekta.item.ModuleItem;
import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.spawner.MissionGenerator;
import vekta.spawner.item.ModuleItemSpawner;

public class ModuleRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 2;
	}

	@Override
	public void setup(Mission mission) {
		mission.add(new ItemReward(new ModuleItem(ModuleItemSpawner.randomModule())));
	}
}
