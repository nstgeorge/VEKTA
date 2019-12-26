package vekta.spawner.dialog;

import vekta.Player;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.ItemTradeButton;
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
		OpinionType opinion = dialog.getPerson().getOpinion(player.getFaction());

		Item item = opinion == OpinionType.GRATEFUL || v.chance(.4F)
				? ItemGenerator.randomItem() // Always give a non-mission item if grateful
				: MissionItemSpawner.randomMissionItem(p -> MissionGenerator.createMission(p, dialog.getPerson()));

		int price = opinion == OpinionType.GRATEFUL || dialog.getPerson().getFaction().isAlly(player.getFaction())
				? 0 : (int)(item.randomPrice() * v.random(.25F, 1));

		dialog.add(new ItemTradeButton(player, item, price));
		if(price > 0) {
			dialog.addResponse("No thanks.");
		}
	}
}
