package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.LandAtObjective;
import vekta.mission.objective.LearnAboutObjective;
import vekta.mission.objective.Objective;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;
import vekta.spawner.knowledge.TopicKnowledgeSpawner;

import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class TopicObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		mission.add(new LandAtObjective(other.findHomeObject()).optional());
		return new LearnAboutObjective(TopicKnowledgeSpawner.randomTopicKnowledge());
	}
}
