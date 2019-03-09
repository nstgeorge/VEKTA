package vekta.spawner;

import vekta.Resources;
import vekta.item.Inventory;
import vekta.item.Item;

import static vekta.Vekta.round;
import static vekta.Vekta.v;

public class ItemGenerator {
	private static final ItemSpawner[] SPAWNERS = Resources.getSubclassInstances(ItemSpawner.class);

	public static Item randomItem() {
		return Weighted.random(SPAWNERS).create();
	}

	public static void addLoot(Inventory inv, int lootTier) {
		addLoot(inv, lootTier, null);
	}

	public static void addLoot(Inventory inv, int lootTier, ItemSpawner spawner) {
		int itemCt = round(v.random(lootTier, lootTier * 2));
		for(int i = 0; i < itemCt; i++) {
			Item item = spawner != null ? spawner.create() : randomItem();
			inv.add(item);
		}
	}

	public interface ItemSpawner extends Weighted {
		boolean isValid(Item item);
		
		Item create();
	}
}
