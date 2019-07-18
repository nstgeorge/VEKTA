package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.OreItem;
import vekta.menu.Menu;

public class OreRefineButton implements ButtonOption {
	private final OreItem item;
	private final Inventory inventory;

	private boolean selected;

	public OreRefineButton(OreItem item, Inventory inventory) {
		this.item = item;
		this.inventory = inventory;
	}

	@Override
	public String getName() {
		return selected ? getItem().getRefined().getName() : getItem().getName();
	}

	@Override
	public int getColor() {
		return getItem().getColor();
	}

	@Override
	public boolean isEnabled() {
		return !selected && inventory.has(getItem());
	}

	public OreItem getItem() {
		return item;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onSelect(Menu menu) {
		if(inventory.remove(getItem())) {
			selected = true;
			inventory.add(getItem().getRefined());
		}
	}
}
