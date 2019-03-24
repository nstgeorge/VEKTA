package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.InventoryMenuHandle;

import static vekta.Vekta.setContext;

public class InventoryOption implements MenuOption {
	private final Inventory inv;

	public InventoryOption(Inventory inv) {
		this.inv = inv;
	}

	@Override
	public String getName() {
		return "Inventory";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), menu.getDefault(), new InventoryMenuHandle(inv));
		Inventory jettison = new Inventory();
		for(Item item : inv) {
			sub.add(new ItemTradeOption(false, inv, jettison, item));
		}
		sub.addDefault();
		setContext(sub);
	}
}
