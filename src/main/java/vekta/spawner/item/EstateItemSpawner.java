package vekta.spawner.item;

import vekta.Resources;
import vekta.economy.Estate;
import vekta.item.EconomyItem;
import vekta.item.EstateItem;
import vekta.item.Item;
import vekta.spawner.ItemGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.settlement.Settlement;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class EstateItemSpawner implements ItemGenerator.ItemSpawner {

	@Override
	public float getWeight() {
		return 0; // Unable to find naturally
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof EstateItem;
	}

	@Override
	public Item create() {
		return randomEstateItem(PersonGenerator.randomHome());
	}

	public static EconomyItem randomEstateItem(Settlement settlement) {
		return randomEstateItem(settlement, v.random(50, 100), sq(v.random(.5F, 2)));
	}

	public static EconomyItem randomEstateItem(Settlement settlement, float size, float valueScale) {
		boolean common = valueScale < 1;
		String name = Resources.generateString("estate_" + (common ? "common" : "rare"));
		return new EstateItem(new Estate(name, settlement, size, valueScale));
	}
}
