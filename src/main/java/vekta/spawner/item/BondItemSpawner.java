package vekta.spawner.item;

import vekta.Faction;
import vekta.item.BondItem;
import vekta.item.Item;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;

public class BondItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .05F; // Rare to find naturally
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof BondItem;
	}

	@Override
	public Item create() {
		Faction faction = FactionGenerator.randomFaction();
		return createBondItem(faction);
	}

	public static BondItem createBondItem(Faction faction) {
		return new BondItem(faction);
	}
}
