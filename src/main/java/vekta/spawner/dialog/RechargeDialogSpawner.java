package vekta.spawner.dialog;

import vekta.item.Inventory;
import vekta.market.Market;
import vekta.menu.Menu;
import vekta.menu.option.RechargeButton;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

import static vekta.Vekta.v;

public class RechargeDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "recharge";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		dialog.add(new RechargeButton(menu.getPlayer(), .5F));

		Inventory inv = new Inventory();
		inv.add((int)v.random(50, 100));

		Market market = new Market(dialog.getPerson().getName(), inv);
		market.setupMenu(menu, false, true);
	}
}
