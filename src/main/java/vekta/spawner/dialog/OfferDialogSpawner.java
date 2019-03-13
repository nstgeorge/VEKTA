package vekta.spawner.dialog;

import vekta.Player;
import vekta.item.Item;
import vekta.menu.option.ItemTradeOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.item.MissionItemSpawner;

public class OfferDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "offer";
	}

	@Override
	public void setup(Player player, Dialog dialog) {
		Item item = MissionItemSpawner.randomMissionItem(p -> MissionGenerator.createMission(p, dialog.getPerson()));
		dialog.add(new ItemTradeOption(player.getInventory(), item, 0));
	}
}
