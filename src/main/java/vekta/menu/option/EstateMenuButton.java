package vekta.menu.option;

import vekta.item.EconomyItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.setContext;

public class EstateMenuButton extends ButtonOption {
	private final Settlement settlement;
	private final Inventory inventory;

	public EstateMenuButton(Settlement settlement, Inventory inventory) {
		this.settlement = settlement;
		this.inventory = inventory;
	}

	@Override
	public String getName() {
		return "Real Estate";
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onSelect(Menu menu) {
		EconomyMenuHandle handle = new EconomyMenuHandle(menu.getPlayer().getInventory(), this::update);
		Menu sub = new Menu(menu, handle);
		update(sub, handle.isBuying());
		setContext(sub);
	}

	private void update(Menu sub, boolean buying) {
		Inventory inv = buying ? getInventory() : sub.getPlayer().getInventory();

		sub.clear();
		for(Item item : inv) {
			if(item instanceof EconomyItem) {
				EconomyItem economyItem = (EconomyItem)item;
				if(economyItem.getEconomy() == getSettlement().getEconomy()) {
					sub.add(new EconomyItemButton(
							sub.getPlayer().getInventory(),
							economyItem,
							0,
							buying,
							(m, b) -> {
								inv.remove(item);
								update(m, b);
							}));
				}
			}
		}
		sub.addDefault();
	}
}
