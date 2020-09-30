package vekta.item;

import vekta.faction.Faction;
import vekta.economy.Economy;

public class BondItem extends EconomyItem {
	private final Faction faction;

	public BondItem(Faction faction) {
		this.faction = faction;
	}

	@Override
	public int getMass() {
		return 1;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public String getName() {
		return getFaction().getName() + " Bonds";
	}

	@Override
	public Economy getEconomy() {
		return getFaction().getEconomy();
	}
}
