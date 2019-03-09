package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.MissionItem;
import vekta.mission.objective.KeepItemObjective;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.v;

public class MissionItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.MISSION;
	}

	@Override
	public Item create() {
		return randomMissionItem(MissionGenerator.createMission(MissionGenerator.randomMissionPerson()));
	}

	public static Item randomMissionItem(Mission mission) {
		MissionItem item = new MissionItem(Resources.generateString("item_mission"), mission);
		Objective objective = new KeepItemObjective(item);
		if(v.chance(.2F)) {
			objective.optional();
		}
		mission.add(objective);
		return item;
	}
}
