package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class FarewellDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "farewell";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		dialog.getPerson().setBusy(true);
	}
}
