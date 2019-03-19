package vekta.item;

import vekta.economy.Economy;

public class CoinItem extends EconomyItem {
	private final float valueScale;

	public CoinItem(String name, Economy economy, float valueScale) {
		super(name, economy);

		this.valueScale = valueScale;
	}

	@Override
	public float getValueScale() {
		return valueScale;
	}
}
