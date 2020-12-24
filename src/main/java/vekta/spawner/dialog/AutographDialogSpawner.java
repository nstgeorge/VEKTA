package vekta.spawner.dialog;

import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.TradeItem;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeButton;
import vekta.person.Dialog;
import vekta.player.Player;
import vekta.spawner.DialogGenerator;
import vekta.spawner.item.TradeItemSpawner;

public class AutographDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "autograph";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		Player player = menu.getPlayer();
		Item item = new TradeItem("Signed " + TradeItemSpawner.randomName(ItemType.COMMON) + " (" + dialog.getPerson().getName() + ")", ItemType.RARE);
		dialog.add(new ItemTradeButton(player, item, 0));
	}
}
