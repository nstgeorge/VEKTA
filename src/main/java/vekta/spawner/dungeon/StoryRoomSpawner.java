package vekta.spawner.dungeon;

import vekta.dungeon.DungeonRoom;
import vekta.dungeon.StoryRoom;
import vekta.spawner.DungeonGenerator;
import vekta.spawner.StoryGenerator;

public class StoryRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return depth >= 4;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		return new StoryRoom(parent, StoryGenerator.createStory(1));
	}
}
