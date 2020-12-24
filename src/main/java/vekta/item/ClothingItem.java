package vekta.item;

import vekta.faction.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.SecurityMenuHandle;
import vekta.menu.option.CustomButton;
import vekta.util.InfoGroup;

public class ClothingItem extends BasicItem {
	private final Faction faction;

	public ClothingItem(String name, ItemType type, Faction faction) {
		super(name, type);

		this.faction = faction;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void onMenu(Menu menu) {
		if(faction != null && menu.getHandle() instanceof SecurityMenuHandle) {
			SecurityMenuHandle handle = (SecurityMenuHandle)menu.getHandle();
			if(handle.getFaction().isAlly(faction)) {
				menu.add(new CustomButton("Wear " + getName(), handle.getNext()).withColor(faction.getColor()));
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		if(faction != null) {
			info.addStat("Faction", faction.getName());
		}
	}
}
