package vekta.item;

import vekta.Faction;

public class BondItem extends Item {

	private final Faction faction;

	public BondItem(Faction faction) {
		super(faction.getName() + " Bonds", ItemType.COMMON);

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public int getColor() {
		return getFaction().getColor();
	}

	@Override
	public int randomPrice() {
		return (int)(super.randomPrice() * getFaction().getValue());
	}
}
