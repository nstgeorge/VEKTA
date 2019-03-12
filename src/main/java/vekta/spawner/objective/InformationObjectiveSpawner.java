package vekta.spawner.objective;

import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.objective.AskAboutObjective;
import vekta.mission.objective.LandAtObjective;
import vekta.mission.objective.Objective;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.v;
import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class InformationObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		Person other = randomMissionPerson(mission.getIssuer());
		mission.add(new LandAtObjective(other.findHomeObject()));
		String[] parts = Resources.generateString("topic").split(":");
		return new AskAboutObjective(parts[0].trim(), parts[1].trim(), v.random(.1F, .5F));
	}
}
