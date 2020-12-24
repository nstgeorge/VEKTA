package vekta.spawner;

import vekta.Resources;
import vekta.dungeon.Dungeon;
import vekta.dungeon.DungeonRoom;
import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.settlement.building.DungeonBuilding;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static processing.core.PApplet.round;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class DungeonGenerator {
	private static final DungeonSpawner[] SPAWNERS = Resources.findSubclassInstances(DungeonSpawner.class);
	private static final int MAX_DEPTH = 10;

	public static Dungeon randomDungeon() {
		List<Dungeon> dungeons = new ArrayList<>();
		for(TerrestrialPlanet planet : getWorld().findObjects(TerrestrialPlanet.class)) {
			for(Settlement settlement : planet.getTerrain().findVisitableSettlements()) {
				for(SettlementPart part : settlement.getParts()) {
					if(part instanceof DungeonBuilding) {
						dungeons.add(((DungeonBuilding)part).getDungeon());
					}
				}
			}
		}
		if(dungeons.isEmpty()) {
			dungeons.add(createDungeon(PersonGenerator.randomHome()));
		}
		return v.random(dungeons);
	}

	public static Dungeon createDungeon(Settlement settlement) {
		Dungeon dungeon = new Dungeon(settlement.getLocation(), Resources.generateString("dungeon"), Resources.generateString("overview_dungeon"));
		addRooms(dungeon.getStartRoom(), 0);
		settlement.add(new DungeonBuilding(dungeon));
		return dungeon;
	}

	public static void addRooms(DungeonRoom room, int depth) {
		// Find valid spawners
		DungeonSpawner[] spawners = Arrays.stream(SPAWNERS)
				.filter(s -> s.isValid(room, depth))
				.toArray(DungeonSpawner[]::new);

		int pathCt = round(v.random(1, 3) * (1 - (float)depth / MAX_DEPTH));

		if(spawners.length > 0) {
			for(int i = 0; i < pathCt; i++) {
				int nextDepth = depth + (int)v.random(3);
				room.addPath(Resources.generateString("dungeon_path"), Weighted.random(spawners).create(room, nextDepth));
			}
		}
	}

	public static DungeonRoom randomRoom() {
		Dungeon dungeon = randomDungeon();
		return v.random(dungeon.getRooms());
	}

	public interface DungeonSpawner extends Weighted {
		boolean isValid(DungeonRoom parent, int depth);

		DungeonRoom create(DungeonRoom parent, int depth);
	}
}
