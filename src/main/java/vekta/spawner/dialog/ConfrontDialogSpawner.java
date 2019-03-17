package vekta.spawner.dialog;

import vekta.Player;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class ConfrontDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "confront";
	}

	@Override
	public void setup(Player player, Dialog dialog) {
		dialog.getPerson().downgradeOpinion(player.getFaction());
	}
}
