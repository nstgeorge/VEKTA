package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.menu.option.BackOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class SecurityDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "security";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		menu.setDefault(new BackOption(menu));
	}
}
