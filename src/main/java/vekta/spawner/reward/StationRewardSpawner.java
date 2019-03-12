package vekta.spawner.reward;

import vekta.item.ModuleItem;
import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.module.station.StationCoreModule;
import vekta.spawner.MissionGenerator;

public class StationRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return true;
	}

	@Override
	public void setup(Mission mission) {
		mission.add(new ItemReward(new ModuleItem(new StationCoreModule(mission.getTier()))));
	}
}
