package vekta.item;

import vekta.economy.Economy;

public class EconomyItem extends Item {
	private final Economy economy;
	private final float valueScale;

	public EconomyItem(String name, Economy economy, float valueScale) {
		super(name, ItemType.ECONOMY);

		this.economy = economy;
		this.valueScale = valueScale;
	}

	public Economy getEconomy() {
		return economy;
	}

	public float getValueScale() {
		return valueScale;
	}

	@Override
	public int randomPrice() {
		return 1 + (int)(getEconomy().getValue() * getValueScale());
	}
}
