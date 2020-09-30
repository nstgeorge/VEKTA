package vekta.spawner.dungeon;

import vekta.dungeon.DungeonRoom;
import vekta.dungeon.InventoryRoom;
import vekta.item.Inventory;
import vekta.spawner.DungeonGenerator;
import vekta.spawner.item.ArtifactItemSpawner;

public class ArtifactRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return depth >= 1;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		Inventory inv = new Inventory();
		inv.add(ArtifactItemSpawner.randomArtifactItem());
		return new InventoryRoom(parent, "Artifact Room", "You find something on the floor.", inv);
	}
}
