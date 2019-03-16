package vekta.spawner.item;

import vekta.Resources;
import vekta.item.EconomyItem;
import vekta.item.Item;
import vekta.spawner.ItemGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.settlement.Settlement;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class EstateItemSpawner implements ItemGenerator.ItemSpawner {
	private static final float VALUE_SCALE = 50;

	@Override
	public float getWeight() {
		return .01F; // Possible but very rare to find naturally
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof EconomyItem;
	}

	@Override
	public Item create() {
		return randomEstateItem(PersonGenerator.randomHome());
	}

	public static EconomyItem randomEstateItem(Settlement settlement) {
		return randomEstateItem(settlement, sq(v.random(.5F, 2)));
	}

	public static EconomyItem randomEstateItem(Settlement settlement, float valueScale) {
		boolean common = valueScale < 1;
		String name = Resources.generateString("estate_" + (common ? "common" : "rare"));
		return new EconomyItem(
				name + " (" + settlement.getName() + ")",
				settlement.getEconomy(),
				valueScale * VALUE_SCALE);
	}
}
