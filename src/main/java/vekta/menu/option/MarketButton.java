package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.market.Market;
import vekta.menu.Menu;
import vekta.menu.handle.TradeMenuHandle;

import java.util.Comparator;
import java.util.Map;

import static vekta.Vekta.setContext;

public class MarketButton implements ButtonOption {
	private final Market market;
	private final boolean buying;
	private final Inventory you;
	private final Map<Item, Integer> offers;

	public MarketButton(Market market, boolean buying, Inventory you, Map<Item, Integer> offers) {
		this.market = market;
		this.buying = buying;
		this.you = you;
		this.offers = offers;
	}

	@Override
	public String getName() {
		return (buying ? "Buy" : "Sell") + " " + market.getName();
	}

	public Inventory getFrom() {
		return buying ? market.getInventory() : you;
	}

	public Inventory getTo() {
		return buying ? you : market.getInventory();
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new TradeMenuHandle(buying, getTo()));
		for(Item item : offers.keySet()) {
			if(getFrom().has(item) && (buying || market.isBuyable(item))) {
				sub.add(new ItemTradeButton(buying, sub.getPlayer(), market.getInventory(), item, offers.get(item)));
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
