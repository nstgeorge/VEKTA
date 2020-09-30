package vekta.spawner.item;

import vekta.Resources;
import vekta.economy.Coin;
import vekta.economy.CoinMarket;
import vekta.economy.Economy;
import vekta.item.CoinItem;
import vekta.item.Item;
import vekta.spawner.ItemGenerator;

import static processing.core.PApplet.sq;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class CoinItemSpawner implements ItemGenerator.ItemSpawner {
	private static final float NEW_COIN_CHANCE = .2F;

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
		CoinMarket market = findCoinMarket();
		return createCoinItem(randomCoin(market), market);
	}

	public static CoinItem createCoinItem(Coin coin, CoinMarket market) {
		return new CoinItem(coin, market);
	}

	public static Coin randomCoin(CoinMarket market) {
		return market.getCoins().isEmpty() || v.chance(NEW_COIN_CHANCE)
				? createCoin(market)
				: v.random(market.getCoins());
	}

	public static Coin createCoin(CoinMarket market) {
		Coin coin = new Coin(Resources.generateString("item_coin"), sq(v.random(.5F, 5)));
		market.getCoins().add(coin);
		return coin;
	}

	public static CoinMarket findCoinMarket() {
		for(Economy economy : getWorld().findObjects(Economy.class)) {
			if(economy.getContainer() instanceof CoinMarket) {
				return (CoinMarket)economy.getContainer();
			}
		}

		// Create singleton coin market if none exists already
		CoinMarket market = new CoinMarket();

		// Add some initial coins
		int coinCt = (int)v.random(10, 15);
		for(int i = 0; i < coinCt; i++) {
			createCoin(market);
		}
		return market;
	}
}
