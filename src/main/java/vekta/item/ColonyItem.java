package vekta.item;

import vekta.faction.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.LocationMenuHandle;
import vekta.menu.option.ColonizeButton;
import vekta.terrain.location.Location;
import vekta.util.InfoGroup;

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
		if(menu.getHandle() instanceof LocationMenuHandle) {
			Location location = ((LocationMenuHandle)menu.getHandle()).getLocation();

			if(location.isHabitable() && !location.getPlanet().isInhabited()) {
				if(menu.getOptions().stream().noneMatch(opt -> opt instanceof ColonizeButton && ((ColonizeButton)opt).getItem().getFaction() == getFaction())) {
					menu.add(new ColonizeButton(this, location));
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Everything you need to start a colony.");
	}
}
