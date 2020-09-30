package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.DeliverItemObjective;
import vekta.mission.objective.Objective;
import vekta.person.Person;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class DeliveryObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return true;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		Person target = randomMissionPerson(mission.getIssuer());
		return new DeliverItemObjective(ItemGenerator.randomItem(), target);
	}
}
