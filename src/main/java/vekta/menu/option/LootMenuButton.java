package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;

import static vekta.Vekta.setContext;

public class LootMenuButton implements ButtonOption {
	private final String name;
	private final Inventory you, them;

	public LootMenuButton(String name, Inventory you, Inventory them) {
		this.name = name;
		this.you = you;
		this.them = them;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new BackButton(menu, () -> {
			if(them.itemCount() == 0) {
				menu.remove(this);
			}
		}), new LootMenuHandle(them));
		for(Item item : them) {
			sub.add(new ItemTradeButton(true, you, them, item));
		}
		sub.addDefault();
		setContext(sub);
	}
}
