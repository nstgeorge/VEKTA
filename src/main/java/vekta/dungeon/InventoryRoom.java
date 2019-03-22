package vekta.dungeon;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeOption;

public class InventoryRoom extends DungeonRoom {
	private final Inventory inventory;

	public InventoryRoom(DungeonRoom parent, String name, String description, Inventory inventory) {
		super(parent, name, description);

		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onEnter(Menu menu) {
		for(Item item : getInventory()) {
			menu.add(new ItemTradeOption(menu.getPlayer().getInventory(), item, 0));
		}
	}
}
