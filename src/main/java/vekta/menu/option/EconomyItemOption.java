package vekta.menu.option;

import vekta.economy.Economy;
import vekta.item.EconomyItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;

import static vekta.Vekta.moneyString;

public class EconomyItemOption implements MenuOption {
	private static final float FEE = .1F;
	
	private final Inventory inventory;
	private final EconomyItem item;
	private final float valueChange;
	private final int price;
	private final boolean buying;
	private final EconomyMenuHandle.EconomyMenuCallback callback;

	public EconomyItemOption(Inventory inv, EconomyItem item, float valueChange, boolean buying, EconomyMenuHandle.EconomyMenuCallback callback) {
		this.inventory = inv;
		this.buying = buying;
		this.callback = callback;

		this.valueChange = buying ? valueChange : -valueChange;
		this.price = Math.round(item.randomPrice() * (1 + (buying ? FEE : -FEE)));

		// If selling, try to replace item with equivalent from inventory
		if(!buying && !inv.has(item)) {
			for(Item other : inv) {
				if(other instanceof EconomyItem && item.getName().equals(other.getName())) {
					this.item = (EconomyItem)other;
					return;
				}
			}
		}
		this.item = item;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public EconomyItem getItem() {
		return item;
	}

	public Economy getEconomy() {
		return getItem().getEconomy();
	}

	@Override
	public String getName() {
		return moneyString(item.getName(), getPrice());
	}

	public float getValueChange() {
		return valueChange;
	}

	public int getPrice() {
		return price;
	}

	public boolean isBuying() {
		return buying;
	}

	@Override
	public int getColor() {
		return getItem().getColor();
	}

	@Override
	public boolean isEnabled() {
		return isBuying() ? getInventory().has(getPrice()) : getInventory().has(getItem());
	}

	@Override
	public void select(Menu menu) {
		if(buying && getInventory().remove(getPrice())) {
			getInventory().add(getItem());
			menu.getPlayer().setBuyPrice(getItem(), getPrice());
		}
		else if(!buying && getInventory().remove(getItem())) {
			getInventory().add(getPrice());
			menu.getPlayer().setBuyPrice(getItem(), 0);
		}
		else {
			return;
		}

		getEconomy().addValue(getValueChange());

		menu.remove(this);
		callback.callback(menu, isBuying());
	}
}
