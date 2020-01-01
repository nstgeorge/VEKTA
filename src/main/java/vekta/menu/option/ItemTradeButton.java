package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.TradeMenuHandle;
import vekta.player.Player;

import java.io.Serializable;

import static vekta.Vekta.*;

public class ItemTradeButton extends ItemButton {
	private final boolean buying;
	private final Player player;
	private final Inventory inv;
	private final int price;

	public ItemTradeButton(Player player, Item item, int price) {
		this(true, player, new Inventory(), item, price);

		inv.add(item);
	}

	public ItemTradeButton(boolean buying, Player player, Inventory inv, Item item) {
		this(buying, player, inv, item, 0);
	}

	public ItemTradeButton(boolean buying, Player player, Inventory inv, Item item, int price) {
		super(item);
		this.buying = buying;
		this.player = player;
		this.inv = inv;
		this.price = price;
	}

	@Override
	public String getName() {
		return moneyString(getItem().getName(), price);
	}

	public Inventory getFrom() {
		return buying ? inv : player.getInventory();
	}

	public Inventory getTo() {
		return buying ? player.getInventory() : inv;
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
		Item item = getItem();
		return getTo().has(price) && getFrom().has(item) && (!(item instanceof TradeAware) || ((TradeAware)item).isTradeEnabled(player));
	}

	@Override
	public String getSelectVerb() {
		return price == 0
				? buying ? "take" : "give"
				: buying ? "buy" : "sell";
	}

	@Override
	public void draw(Menu menu, int index) {
		super.draw(menu, index);

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
		Item item = getItem();
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

	public interface TradeAware extends Serializable {
		default boolean isTradeEnabled(Player player) {
			return true;
		}
	}
}
