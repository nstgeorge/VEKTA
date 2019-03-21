package vekta.item;

import vekta.Faction;
import vekta.economy.Economy;

public class BondItem extends EconomyItem {
	private final Faction faction;

	public BondItem(Faction faction) {
		super(faction.getName() + " Bonds");

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public Economy getEconomy() {
		return getFaction().getEconomy();
	}
}
