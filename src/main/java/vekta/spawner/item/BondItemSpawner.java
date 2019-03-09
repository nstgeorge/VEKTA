package vekta.spawner.item;

import vekta.item.BondItem;
import vekta.item.Item;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;

public class BondItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof BondItem;
	}

	@Override
	public Item create() {
		return new BondItem(FactionGenerator.randomFaction());
	}
}
