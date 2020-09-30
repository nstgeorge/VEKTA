package vekta.item;

import vekta.economy.Economy;
import vekta.economy.Estate;
import vekta.util.InfoGroup;

public class EstateItem extends EconomyItem {
	private final Estate estate;

	public EstateItem(Estate estate) {
		this.estate = estate;
	}

	public Estate getEstate() {
		return estate;
	}

	@Override
	public String getName() {
		return getEstate().getName()/* + " (" + estate.getSettlement().getName() + ")"*/;
	}

	@Override
	public Economy getEconomy() {
		return getEstate().getSettlement().getEconomy();
	}

	@Override
	public int getMass() {
		return 1;
	}

	@Override
	public float getValueScale() {
		return getEstate().getSize() * getEstate().getValue();
	}

	@Override
	public void onInfo(InfoGroup info) {
		super.onInfo(info);

		info.addStat("Settlement", getEstate().getSettlement().getName());
	}
}
