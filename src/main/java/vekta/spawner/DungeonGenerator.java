package vekta.spawner;

import vekta.Resources;
import vekta.dungeon.Dungeon;
import vekta.dungeon.DungeonRoom;

import java.util.Arrays;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class DungeonGenerator {
	private static final DungeonSpawner[] SPAWNERS = Resources.getSubclassInstances(DungeonSpawner.class);
	private static final int MAX_DEPTH = 10;

	public static Dungeon createDungeon() {
		Dungeon dungeon = new Dungeon(Resources.generateString("dungeon"), Resources.generateString("overview_dungeon"));
		addRooms(dungeon.getStartRoom(), 0);
		return dungeon;
	}

	public static void addRooms(DungeonRoom room, int depth) {
		// Find valid spawners
		DungeonSpawner[] spawners = Arrays.stream(SPAWNERS)
				.filter(s -> s.isValid(room, depth))
				.toArray(DungeonSpawner[]::new);

		int pathCt = round(v.random(1, 2) * (1 - (float)depth / MAX_DEPTH));

		if(spawners.length > 0) {
			for(int i = 0; i < pathCt; i++) {
				int nextDepth = depth + (int)v.random(3);
				room.addPath(Resources.generateString("dungeon_path"), Weighted.random(spawners).create(room, nextDepth));
			}
		}
	}

	public interface DungeonSpawner extends Weighted {
		boolean isValid(DungeonRoom parent, int depth);

		DungeonRoom create(DungeonRoom parent, int depth);
	}
}
