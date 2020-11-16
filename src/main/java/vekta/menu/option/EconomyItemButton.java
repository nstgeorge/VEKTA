package vekta.menu.option;

import vekta.economy.Economy;
import vekta.item.EconomyItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;
import vekta.menu.handle.MenuHandle;

import static vekta.Vekta.*;

public class EconomyItemButton extends ButtonOption {
	private static final float FEE = 1;

	private final Inventory inventory;
	private final EconomyItem item;
	private final float valueChange;
	private final int price;
	private final boolean buying;
	private final EconomyMenuHandle.EconomyMenuCallback callback;

	public EconomyItemButton(Inventory inv, EconomyItem item, float valueChange, boolean buying, EconomyMenuHandle.EconomyMenuCallback callback) {
		this.inventory = inv;
		this.buying = buying;
		this.callback = callback;

		this.valueChange = buying ? valueChange : -valueChange;
		this.price = Math.round(item.randomPrice() + (buying ? FEE : -FEE));

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

	public int countItems() {
		int ct = 0;
		for(Item other : getInventory()) {
			if(getItem().getName().equals(other.getName())) {
				ct++;
			}
		}
		return ct;
	}

	@Override
	public void render(Menu menu, int index) {
		super.render(menu, index);

		MenuHandle handle = menu.getHandle();

		int ct = countItems();
		if(ct > 0) {
			v.textSize(24);
			v.fill(UI_COLOR);
			v.text("x" + ct, handle.getItemX() + handle.getItemWidth() / 2F + 50, handle.getItemY(index) + 6);
		}
	}

	@Override
	public void onSelect(Menu menu) {
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
