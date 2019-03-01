package vekta.menu;

import vekta.item.Inventory;
import vekta.item.Item;

public class TradeOption implements MenuOption {
	private final Inventory from, to;
	private final Item item;
	private final int price;
	private final boolean transfer;

	public TradeOption(Inventory from, Inventory to, Item item, int price, boolean transfer) {
		this.from = from;
		this.to = to;
		this.item = item;
		this.price = price;
		this.transfer = transfer;
	}

	@Override
	public String getName() {
		return item.getName() + " [" + price + " G]";
	}

	public Item getItem() {
		return item;
	}

	@Override
	public void select(Menu menu) {
		if(to.has(price) && from.has(item)) {
			to.remove(price);
			from.remove(item);
			from.add(price);
			if(transfer) {
				to.add(item);
			}
			menu.remove(this);
		}
	}
}
