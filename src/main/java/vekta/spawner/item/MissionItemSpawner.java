package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.MissionItem;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

public class MissionItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.MISSION;
	}

	@Override
	public Item create() {
		return randomMissionItem(p -> MissionGenerator.createMission(p, MissionGenerator.randomMissionPerson()));
	}

	public static Item randomMissionItem(MissionItem.MissionProvider provider) {
		return new MissionItem(Resources.generateString("item_mission"), provider);
	}
}
