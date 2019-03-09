package vekta.spawner.item;

import vekta.item.BondItem;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.FactionGenerator;
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
		return new BondItem(FactionGenerator.randomFaction());
	}
}
