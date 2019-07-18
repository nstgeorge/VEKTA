package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.DialogObjective;
import vekta.mission.objective.Objective;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;

import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class ConfrontObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		Person person = randomMissionPerson(mission.getIssuer());
		return new DialogObjective("Confront", mission.getIssuer().getName() + " sent me to chat with you.", person.createDialog("confronted"));
	}
}
