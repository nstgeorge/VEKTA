package vekta.spawner.reward;

import vekta.mission.Mission;
import vekta.mission.reward.FollowerReward;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;

public class FollowerRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 2 && mission.getTier() <= 3;
	}

	@Override
	public void setup(Mission mission) {
		Person person = new TemporaryPerson(PersonGenerator.randomBossName(), mission.getPlayer().getFaction());
		mission.add(new FollowerReward(person));
	}
}
