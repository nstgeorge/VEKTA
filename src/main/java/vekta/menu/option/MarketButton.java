package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.TradeMenuHandle;
import vekta.terrain.building.MarketBuilding;

import java.util.Comparator;
import java.util.Map;

import static vekta.Vekta.setContext;

public class MarketButton implements ButtonOption {
	private final MarketBuilding building;
	private final boolean buying;
	private final Inventory you, them;
	private final Map<Item, Integer> offers;

	public MarketButton(MarketBuilding building, boolean buying, Inventory you, Map<Item, Integer> offers) {
		this.building = building;
		this.buying = buying;
		this.you = you;
		this.them = building.getInventory();
		this.offers = offers;
	}

	@Override
	public String getName() {
		return (buying ? "Buy" : "Sell") + " " + building.getGenericName();
	}

	public Inventory getFrom() {
		return buying ? them : you;
	}

	public Inventory getTo() {
		return buying ? you : them;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new TradeMenuHandle(buying, getTo()));
		for(Item item : offers.keySet()) {
			if(getFrom().has(item) && (buying || building.canSell(item))) {
				sub.add(new ItemTradeButton(buying, you, them, item, offers.get(item), true));
			}
		}
		sub.getOptions().sort(Comparator.comparingInt(opt -> {
			if(opt instanceof ItemTradeButton) {
				return ((ItemTradeButton)opt).getProfit(sub.getPlayer()) * (buying ? 1 : -1);
			}
			return 0;
		}));
		sub.addDefault();
		setContext(sub);
	}
}
