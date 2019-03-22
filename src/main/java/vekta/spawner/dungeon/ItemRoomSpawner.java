package vekta.spawner.dungeon;

import vekta.dungeon.DungeonRoom;
import vekta.dungeon.InventoryRoom;
import vekta.item.Inventory;
import vekta.spawner.DungeonGenerator;
import vekta.spawner.ItemGenerator;

public class ItemRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return true;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		Inventory inv = new Inventory();
		ItemGenerator.addLoot(inv, 1);
		return new InventoryRoom(parent, "Storage Room", "You find what appears to be a long-abandoned storage closet.", inv);
	}
}
