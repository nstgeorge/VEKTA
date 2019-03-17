package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.spawner.item.EstateItemSpawner;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

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
		Settlement home = mission.getIssuer() instanceof Person && v.chance(.8F)
				? ((Person)mission.getIssuer()).findHome()
				: PersonGenerator.randomHome();
		mission.add(new ItemReward(EstateItemSpawner.randomEstateItem(home)));
	}
}
