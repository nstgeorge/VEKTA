package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;
import vekta.spawner.item.OreItemSpawner;
import vekta.terrain.LandingSite;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class ExtractMenuButton extends ButtonOption {
	private final LandingSite site;
	private final int amount;

	public ExtractMenuButton(LandingSite site, int amount) {
		this.site = site;
		this.amount = amount;
	}

	@Override
	public String getName() {
		return "Extract";
	}

	@Override
	public void onSelect(Menu menu) {
		Inventory loot = new Inventory();
		Menu sub = new Menu(menu, new LootMenuHandle(loot));
		int ct = (int)v.random(amount) + 1;
		for(int i = 0; i < ct; i++) {
			Item item = OreItemSpawner.randomOre(site.getParent().getName());
			loot.add(item);
			sub.add(new ItemTradeButton(true, menu.getPlayer(), loot, item));
		}
		sub.addDefault();
		setContext(sub);

		site.getTerrain().remove("Mineable");
		menu.remove(this);
	}
}
