package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class FollowerDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "follower";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		
	}
}
