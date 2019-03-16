package vekta.spawner.item;

import vekta.Faction;
import vekta.item.EconomyItem;
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
		return item instanceof EconomyItem;
	}

	@Override
	public Item create() {
		Faction faction = FactionGenerator.randomFaction();
		return createBondItem(faction);
	}

	public static EconomyItem createBondItem(Faction faction) {
		return new EconomyItem(faction.getName() + " Bonds", faction.getEconomy(), 1);
	}
}
