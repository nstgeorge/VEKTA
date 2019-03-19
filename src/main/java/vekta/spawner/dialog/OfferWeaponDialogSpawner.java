package vekta.spawner.dialog;

import vekta.Player;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.item.WeaponItemSpawner;

public class OfferWeaponDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "offer_weapon";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		Player player = menu.getPlayer();
		Item item = WeaponItemSpawner.randomWeapon();
		dialog.add(new ItemTradeOption(player.getInventory(), item, 0));
	}
}
