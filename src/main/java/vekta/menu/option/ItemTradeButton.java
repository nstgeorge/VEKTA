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
import vekta.menu.handle.TradeMenuHandle;

import static vekta.Vekta.*;

public class ItemTradeButton implements ButtonOption, LayoutBuilder {
	private final boolean buying;
	private final Inventory you, them;
	private final Item item;
	private final int price;

	public ItemTradeButton(Inventory you, Item item, int price) {
		this(true, you, new Inventory(), item, price);

		them.add(item);
	}

	public ItemTradeButton(boolean buying, Inventory you, Inventory them, Item item) {
		this(buying, you, them, item, 0);
	}

	public ItemTradeButton(boolean buying, Inventory you, Inventory them, Item item, int price) {
		this.buying = buying;
		this.you = you;
		this.them = them;
		this.item = item;
		this.price = price;
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
		return price == 0
				? buying ? "take" : "give"
				: buying ? "buy" : "sell";
	}

	@Override
	public void draw(Menu menu, int index) {
		ButtonOption.super.draw(menu,index);
		
		if(menu.getHandle() instanceof TradeMenuHandle) {
			TradeMenuHandle handle = (TradeMenuHandle)menu.getHandle();

			if(!handle.isLeftSide()) {
				int profit = getProfit(menu.getPlayer());

				v.fill(profit > 0 ? UI_COLOR : 100);
				v.text((profit > 0 ? "+" : "") + quantityString(profit), handle.getItemX() + handle.getItemWidth() / 2F + 50, handle.getItemY(index));
			}
		}
	}

	@Override
	public void onSelect(Menu menu) {
		Inventory from = getFrom();
		Inventory to = getTo();
		to.remove(price);
		from.remove(item);
		from.add(price);
		to.add(item);
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

		info.onLayout(layout);
		layout.add(new TextDisplay(moneyString(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + getSelectVerb(), price)))
				.customize().color(100);
	}
}
