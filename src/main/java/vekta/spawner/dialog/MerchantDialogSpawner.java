package vekta.spawner.dialog;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.TradeItem;
import vekta.market.Market;
import vekta.market.TemporaryMarket;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeButton;
import vekta.menu.option.MarketButton;
import vekta.person.Dialog;
import vekta.player.Player;
import vekta.spawner.DialogGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.TradeItemSpawner;

import static vekta.Vekta.v;

public class MerchantDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "merchant";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		Player player = menu.getPlayer();

		Market market = new TemporaryMarket("Goods", new Inventory());

		market.getInventory().add((int)v.random(30, 60));
		ItemGenerator.addLoot(market.getInventory(), 1, ItemGenerator.randomItemSpawner());

		market.setupMenu(menu, true, false /*true*/);
	}
}
