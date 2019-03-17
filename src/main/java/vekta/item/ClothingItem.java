package vekta.item;

import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.SecurityMenuHandle;
import vekta.menu.option.CustomOption;

public class ClothingItem extends Item {
	private final Faction faction;

	public ClothingItem(String name, ItemType type, Faction faction) {
		super(name, type);

		this.faction = faction;
	}

	@Override
	public String getName() {
		return super.getName() + (faction != null ? " (" + faction.getName() + ")" : "");
	}

	@Override
	public void onMenu(Menu menu) {
		if(faction != null && menu.getHandle() instanceof SecurityMenuHandle) {
			SecurityMenuHandle handle = (SecurityMenuHandle)menu.getHandle();
			if(handle.getFaction().isAlly(faction)) {
				menu.add(new CustomOption("Wear " + getName(), handle.getNext()).withColor(faction.getColor()));
			}
		}
	}
}
