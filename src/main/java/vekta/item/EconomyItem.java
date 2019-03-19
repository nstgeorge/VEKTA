package vekta.item;

import vekta.economy.Economy;

public abstract class EconomyItem extends Item {
	private final Economy economy;

	public EconomyItem(String name, Economy economy) {
		super(name, ItemType.ECONOMY);

		this.economy = economy;
	}

	public Economy getEconomy() {
		return economy;
	}

	public float getValueScale() {
		return 1;
	}

	@Override
	public int randomPrice() {
		return 1 + (int)(getEconomy().getValue() * getValueScale());
	}
}
