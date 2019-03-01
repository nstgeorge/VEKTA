package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.ScavengeMenuHandle;

import static vekta.Vekta.setContext;

public class ScavengeMenuOption implements MenuOption {
	private final Inventory you, them;

	public ScavengeMenuOption(Inventory you, Inventory them) {
		this.you = you;
		this.them = them;
	}

	@Override
	public String getName() {
		return "Scavenge";
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(new ScavengeMenuHandle(new BackOption(menu), them));
		for(Item item : them) {
			sub.add(new TradeOption(true, you, them, item));
		}
		sub.addDefault();
		setContext(sub);
	}
}
