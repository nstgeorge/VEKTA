package vekta.spawner.dialog;

import vekta.Player;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeOption;
import vekta.person.Dialog;
import vekta.person.OpinionType;
import vekta.spawner.DialogGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.item.MissionItemSpawner;

import static vekta.Vekta.v;

public class OfferDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "offer";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		Player player = menu.getPlayer();
		Item item = dialog.getPerson().getOpinion(player.getFaction()) == OpinionType.GRATEFUL && v.chance(.4F)
				? ItemGenerator.randomItem() // Occasionally give a useful item if grateful
				: MissionItemSpawner.randomMissionItem(p -> MissionGenerator.createMission(p, dialog.getPerson()));
		dialog.add(new ItemTradeOption(player.getInventory(), item, 0));
	}
}
