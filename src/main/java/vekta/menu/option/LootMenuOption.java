package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;

import static vekta.Vekta.setContext;

public class LootMenuOption implements MenuOption {
	private final String name;
	private final Inventory you, them;

	public LootMenuOption(String name, Inventory you, Inventory them) {
		this.name = name;
		this.you = you;
		this.them = them;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(menu.getPlayer(), new LootMenuHandle(new BackOption(menu, () -> {
			if(them.size() == 0) {
				menu.remove(this);
			}
		}), them));
		for(Item item : them) {
			sub.add(new ItemTradeOption(true, you, them, item));
		}
		sub.addDefault();
		setContext(sub);
	}
}
