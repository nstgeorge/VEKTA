package vekta.menu;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.handle.TradeMenuHandle;

import java.util.Map;

import static vekta.Vekta.setContext;

public class TradeMenuOption implements MenuOption {
	private final boolean buying;
	private final Inventory you, them;
	private final Map<Item, Integer> offers;

	public TradeMenuOption(boolean buying, Inventory you, Inventory them, Map<Item, Integer> offers) {
		this.buying = buying;
		this.you = you;
		this.them = them;
		this.offers = offers;
	}

	@Override
	public String getName() {
		return (buying ? "Buy" : "Sell") + " Goods";
	}

	public Inventory getFrom() {
		return buying ? them : you;
	}

	public Inventory getTo() {
		return buying ? you : them;
	}

	@Override
	public void select(Menu menu) {
		MenuOption def = new BackOption(menu);
		Menu sub = new Menu(new TradeMenuHandle(def, you, them));
		for(Item item : offers.keySet()) {
			if(getFrom().has(item)) {
				sub.add(new TradeOption(getFrom(), getTo(), item, offers.get(item), true));
			}
		}
		sub.add(def);
		setContext(sub);
	}
}
