package vekta.menu.option;

import vekta.ItemGenerator;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.LootMenuHandle;
import vekta.terrain.LandingSite;

import static vekta.Vekta.*;

public class ExtractMenuOption implements MenuOption {
	private final LandingSite site;
	private final Inventory inv;
	private final int amount;

	public ExtractMenuOption(LandingSite site, Inventory inv, int amount) {
		this.site = site;
		this.inv = inv;
		this.amount = amount;
	}

	@Override
	public String getName() {
		return "Extract";
	}

	@Override
	public void select(Menu menu) {
		Inventory loot = new Inventory();
		Menu sub = new Menu(menu.getPlayer(), new LootMenuHandle(new BackOption(menu), loot));
		int ct = (int)v.random(amount) + 1;
		for(int i = 0; i < ct; i++) {
			Item item = ItemGenerator.randomOre(site.getParent().getName());
			loot.add(item);
			sub.add(new ItemTradeOption(true, inv, loot, item));
		}
		sub.addDefault();
		setContext(sub);

		site.getTerrain().remove("Mineable");
		menu.remove(this);
	}
}
