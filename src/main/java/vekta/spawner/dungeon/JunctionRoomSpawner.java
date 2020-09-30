package vekta.spawner.dungeon;

import vekta.Resources;
import vekta.dungeon.DungeonRoom;
import vekta.spawner.DungeonGenerator;

public class JunctionRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return 4;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return true;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		String[] args = Resources.generateString("room").split(":", 2);
		DungeonRoom room = new DungeonRoom(
				parent.getDungeon(),
				args[0].trim(),
				args[1].trim());
		DungeonGenerator.addRooms(room, depth + 1);
		return room;
	}
}
