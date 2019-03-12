package vekta.spawner.item;

import vekta.item.ColonyItem;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.ItemGenerator;

public class ColonyItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.COLONY;
	}

	@Override
	public Item create() {
		return randomColonyItem();
	}

	public static Item randomColonyItem() {
		// TODO: add more items
		return new ColonyItem();
	}
}
