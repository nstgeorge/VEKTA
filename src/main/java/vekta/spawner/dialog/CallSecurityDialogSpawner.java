package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.menu.option.DialogButton;
import vekta.menu.option.MenuOption;
import vekta.menu.option.QuicktimeButton;
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
		menu.setDefault(new DialogButton("Talk to Security", security));

		dialog.add(new QuicktimeButton(3, "Run Away", leave::onSelect));
	}
}
