package vekta.item;

import vekta.faction.Faction;
import vekta.util.InfoGroup;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.ColonizeButton;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

public class ColonyItem extends Item {
	private final Faction faction;

	public ColonyItem() {
		this(null);
	}

	public ColonyItem(Faction faction) {
		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public String getName() {
		return "Colony Kit" + (faction != null ? " (" + faction + ")" : "");
	}

	@Override
	public ItemType getType() {
		return ItemType.COLONY;
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();
			Terrain terrain = site.getTerrain();
			if(terrain.hasFeature("Habitable") && !terrain.isInhabited()) {
				if(menu.getOptions().stream().noneMatch(opt -> opt instanceof ColonizeButton && ((ColonizeButton)opt).getItem().getFaction() == getFaction())) {
					menu.add(new ColonizeButton(this, site));
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Everything you need to start a colony.");
	}
}
