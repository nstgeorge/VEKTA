package vekta.spawner.objective;

import vekta.item.Item;
import vekta.mission.Mission;
import vekta.mission.objective.DeliverItemObjective;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SearchForItemObjective;
import vekta.person.Person;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.v;
import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class SearchObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		Item item = ItemGenerator.randomItem();
		Person person = mission.getIssuer() instanceof Person && v.chance(.8F)
				? (Person)mission.getIssuer()
				: randomMissionPerson();
		mission.add(new SearchForItemObjective(item, v.random(.1F, .5F)));
		return new DeliverItemObjective(item, person);
	}
}
