package vekta.item;

import vekta.economy.Coin;
import vekta.economy.CoinMarket;
import vekta.economy.Economy;

public class CoinItem extends EconomyItem {
	private final Coin coin;
	private final CoinMarket market;

	public CoinItem(Coin coin, CoinMarket market) {
		super(coin.getName());

		this.coin = coin;
		this.market = market;
	}

	public Coin getCoin() {
		return coin;
	}

	public CoinMarket getMarket() {
		return market;
	}

	@Override
	public Economy getEconomy() {
		return getMarket().getEconomy();
	}

	@Override
	public float getValueScale() {
		return getCoin().getValueScale();
	}
}
