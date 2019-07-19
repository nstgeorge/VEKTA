package vekta.menu.option;

import vekta.InfoGroup;
import vekta.KeyBinding;
import vekta.Player;
import vekta.Settings;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;

import static vekta.Vekta.moneyString;

public class ItemTradeButton implements ButtonOption, LayoutBuilder {
	private final boolean buying;
	private final Inventory you, them;
	private final Item item;
	private final int price;
	private final boolean transfer;

	public ItemTradeButton(Inventory inv, Item item, int price) {
		this(true, inv, new Inventory(), item, price, true);

		them.add(item);
	}

	public ItemTradeButton(boolean buying, Inventory you, Inventory them, Item item) {
		this(buying, you, them, item, 0, true);
	}

	public ItemTradeButton(boolean buying, Inventory you, Inventory them, Item item, int price, boolean transfer) {
		this.buying = buying;
		this.you = you;
		this.them = them;
		this.item = item;
		this.price = price;
		this.transfer = transfer;
	}

	@Override
	public String getName() {
		return moneyString(item.getName(), price);
	}

	@Override
	public int getColor() {
		return getItem().getColor();
	}

	public Item getItem() {
		return item;
	}

	public Inventory getFrom() {
		return buying ? them : you;
	}

	public Inventory getTo() {
		return buying ? you : them;
	}

	public int getPrice() {
		return price;
	}

	public int getProfit(Player player) {
		int buyPrice = player.getBuyPrice(getItem());
		return getPrice() - buyPrice;
	}

	@Override
	public boolean isEnabled() {
		return getTo().has(price) && getFrom().has(item);
	}

	@Override
	public String getSelectVerb() {
		return price == 0 ? "transfer" : buying ? "buy" : "sell";
	}

	@Override
	public void onSelect(Menu menu) {
		Inventory from = getFrom();
		Inventory to = getTo();
		to.remove(price);
		from.remove(item);
		from.add(price);
		if(transfer) {
			to.add(item);
		}
		if(buying) {
			menu.getPlayer().setBuyPrice(item, price);
		}
		menu.remove(this);
	}

	@Override
	public void onLayout(Layout layout) {
		layout.add(new TextDisplay(getItem().getName()))
				.customize().fontSize(32);

		InfoGroup info = new InfoGroup();
		info.addStat("Mass", item.getMass());
		item.onInfo(info);
		for(String line : info) {
			// TODO: convert to InfoDisplay class
			layout.add(new TextDisplay(line));
		}

		layout.add(new TextDisplay(moneyString(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + getSelectVerb(), price)))
				.customize().color(100);
	}
}
