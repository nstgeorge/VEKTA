package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;

import static vekta.Vekta.setContext;

public class LootMenuButton extends ButtonOption {
	private final String name;
	private final Inventory inv;

	public LootMenuButton(String name, Inventory inv) {
		this.name = name;
		this.inv = inv;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new BackButton(menu, () -> {
			if(inv.itemCount() == 0) {
				menu.remove(this);
			}
		}), new LootMenuHandle(inv));
		for(Item item : inv) {
			sub.add(new ItemTradeButton(true, menu.getPlayer(), inv, item));
		}
		sub.addDefault();
		setContext(sub);
	}
}
