package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;

public class JettisonButton implements ButtonOption {
	private final Item item;
	private final Inventory inv;

	public JettisonButton(Item item, Inventory inv) {
		this.item = item;
		this.inv = inv;
	}

	@Override
	public String getName() {
		return item.getName();
	}

	@Override
	public int getColor() {
		return item.getColor();
	}

	@Override
	public String getSelectVerb() {
		return "jettison";
	}

	@Override
	public void onSelect(Menu menu) {
		inv.remove(item);
		menu.remove(this);
	}
}
