package vekta.item;

import vekta.economy.Estate;

public class EstateItem extends EconomyItem {
	private final Estate estate;

	public EstateItem(Estate estate) {
		super(estate.getName() + " (" + estate.getSettlement().getName() + ")", estate.getSettlement().getEconomy());

		this.estate = estate;
	}

	public Estate getEstate() {
		return estate;
	}

	@Override
	public float getValueScale() {
		return getEstate().getSize() * getEstate().getValue();
	}
}
