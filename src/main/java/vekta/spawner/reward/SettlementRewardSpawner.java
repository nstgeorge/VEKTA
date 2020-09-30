package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.SettlementReward;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.settlement.Settlement;

public class SettlementRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 4;
	}

	@Override
	public void setup(Mission mission) {
		Settlement settlement = null;
		if(mission.getIssuer() instanceof Person) {
			Person person = (Person)mission.getIssuer();
			if(person.hasHome() && person.getFaction() != mission.getPlayer().getFaction()) {
				settlement = person.findHome();
			}
		}
		if(settlement == null) {
			PersonGenerator.randomHome();
		}

		mission.add(new SettlementReward(settlement));
	}
}
