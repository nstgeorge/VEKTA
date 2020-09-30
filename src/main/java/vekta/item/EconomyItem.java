package vekta.item;

import vekta.economy.Economy;

public abstract class EconomyItem extends Item {

	public abstract Economy getEconomy();

	@Override
	public ItemType getType() {
		return ItemType.ECONOMY;
	}

	public float getValueScale() {
		return 1;
	}

	@Override
	public int randomPrice() {
		return 1 + (int)(getEconomy().getValue() * getValueScale());
	}
}
