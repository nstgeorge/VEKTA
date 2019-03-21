package vekta.item;

import vekta.economy.Economy;

public abstract class EconomyItem extends Item {

	public EconomyItem(String name) {
		super(name, ItemType.ECONOMY);
	}

	public abstract Economy getEconomy();

	public float getValueScale() {
		return 1;
	}

	@Override
	public int randomPrice() {
		return 1 + (int)(getEconomy().getValue() * getValueScale());
	}
}
