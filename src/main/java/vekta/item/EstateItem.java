package vekta.item;

import vekta.economy.Economy;
import vekta.economy.Estate;

public class EstateItem extends EconomyItem {
	private final Estate estate;

	public EstateItem(Estate estate) {
		super(estate.getName() + " (" + estate.getSettlement().getName() + ")");

		this.estate = estate;
	}

	public Estate getEstate() {
		return estate;
	}

	@Override
	public Economy getEconomy() {
		return getEstate().getSettlement().getEconomy();
	}

	@Override
	public float getValueScale() {
		return getEstate().getSize() * getEstate().getValue();
	}
}
