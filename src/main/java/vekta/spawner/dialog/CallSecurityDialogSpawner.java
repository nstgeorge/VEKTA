package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.menu.option.MenuOption;
import vekta.menu.option.QuicktimeOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;

public class CallSecurityDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "call_security";
	}
	
	@Override
	public void setup(Menu menu, Dialog dialog) {
		MenuOption leave = menu.getDefault();

		Dialog security = SecurityDialogSpawner.randomSecurityDialog(dialog.getPerson().getFaction(), menu.getDefault());
		menu.setDefault(new DialogOption("Talk to Security", security));

		dialog.add(new QuicktimeOption(3, "Run Away", leave::onSelect));
	}
}
