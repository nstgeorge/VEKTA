package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.JunkItem;
import vekta.spawner.ItemGenerator;

public class JunkItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 2;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.JUNK;
	}

	@Override
	public Item create() {
		return randomJunkItem();
	}

	public static JunkItem randomJunkItem() {
		return new JunkItem(Resources.generateString("item_junk"));
	}
}
