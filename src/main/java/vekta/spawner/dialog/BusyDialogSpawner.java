package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class BusyDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "busy";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		dialog.getPerson().setBusy(true);
	}
}
