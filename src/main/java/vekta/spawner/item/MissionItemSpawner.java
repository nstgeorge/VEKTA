package vekta.spawner.item;

import vekta.Resources;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.item.Item;
import vekta.item.MissionItem;
import vekta.mission.Mission;

public class MissionItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public Item create() {
		return randomMissionItem(MissionGenerator.createMission(PersonGenerator.createPerson()));
	}

	public static Item randomMissionItem(Mission mission) {
		return new MissionItem(Resources.generateString("item_mission"), mission);
	}
}
