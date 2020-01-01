package vekta.spawner.dungeon;

import vekta.dungeon.BuildingRoom;
import vekta.dungeon.DungeonRoom;
import vekta.spawner.DeityGenerator;
import vekta.spawner.DungeonGenerator;
import vekta.terrain.building.TempleBuilding;

public class TempleRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return depth >= 2;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		return new BuildingRoom(parent, new TempleBuilding(DeityGenerator.randomDeity()), "You discover the remnants of an ancient temple.");
	}
}
