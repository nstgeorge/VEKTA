package vekta.item;

import vekta.Faction;

public class BondItem extends Item {

	public BondItem(Faction faction) {
		super(faction.getName() + " Bonds", ItemType.COMMON);
	}
	
	// Todo: add market fluctuations based on player actions
}
