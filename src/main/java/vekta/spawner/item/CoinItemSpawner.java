package vekta.spawner.item;

import vekta.Resources;
import vekta.economy.Economy;
import vekta.item.CoinItem;
import vekta.item.Item;
import vekta.spawner.ItemGenerator;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class CoinItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .2F; // Somewhat rare to find naturally
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof CoinItem;
	}

	@Override
	public Item create() {
		Economy economy = new Economy(1); // TODO: global coin economy
		return createCoinItem(economy);
	}

	public static CoinItem createCoinItem(Economy economy) {
		return new CoinItem(Resources.generateString("item_coin"), economy, sq(v.random(1, 10)));
	}
}
