package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.MissionItem;
import vekta.mission.Mission;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

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
		return new MissionItem(Resources.generateString("item_mission"), mission);
	}
}
