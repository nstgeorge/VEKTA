package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class ConfrontDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "confront";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		dialog.getPerson().downgradeOpinion(menu.getPlayer().getFaction());
	}
}
