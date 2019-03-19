package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.AssassinateObjective;
import vekta.mission.objective.Objective;
import vekta.person.Person;

import static vekta.spawner.MissionGenerator.ObjectiveSpawner;
import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class AssassinateObjectiveSpawner implements ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		Person person = randomMissionPerson(mission.getIssuer());
		return new AssassinateObjective(person);
	}
}
