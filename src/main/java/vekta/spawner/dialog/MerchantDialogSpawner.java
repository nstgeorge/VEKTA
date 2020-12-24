package vekta.spawner.dialog;

import vekta.item.Inventory;
import vekta.market.Market;
import vekta.market.TemporaryMarket;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.ItemGenerator;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class MerchantDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "merchant";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {

		Market market = new TemporaryMarket("Goods", new Inventory());

		market.getInventory().add((int)v.random(30, 60));
		ItemGenerator.addLoot(market.getInventory(), 1, ItemGenerator.randomItemSpawner());

		List<MenuOption> items = new ArrayList<>(menu.getOptions());
		menu.clear();

		market.setupMenu(menu, true, false /*true*/);

		for(MenuOption item : items) {
			menu.add(item);
		}
	}
}
