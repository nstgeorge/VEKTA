package vekta.item;

import vekta.Faction;

public class BondItem extends EconomyItem {
	private final Faction faction;

	public BondItem(Faction faction) {
		super(faction.getName() + " Bonds", faction.getEconomy());

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}
}
