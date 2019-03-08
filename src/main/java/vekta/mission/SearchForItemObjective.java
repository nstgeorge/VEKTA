package vekta.mission;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;
import vekta.object.SpaceObject;

import static vekta.Vekta.v;

public class SearchForItemObjective extends Objective {
	private final Item item;
	private final float rarity;

	public SearchForItemObjective(Item item, float rarity) {
		this.item = item;
		this.rarity = rarity;
	}

	public Item getItem() {
		return item;
	}

	public float getRarity() {
		return rarity;
	}

	@Override
	public String getName() {
		return "Search for " + getItem().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LootMenuHandle) {
			Inventory inv = ((LootMenuHandle)menu.getHandle()).getInventory();
			if(v.chance(getRarity())) {
				getMission().add(new ObtainItemObjective(getItem()));
				inv.add(getItem());
				complete();
			}
		}
	}
}
