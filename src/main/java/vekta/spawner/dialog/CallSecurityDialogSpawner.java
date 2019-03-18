package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.menu.option.CustomOption;
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
		MenuOption def = menu.getDefault();

		Dialog security = DialogGenerator.randomSecurityDialog(dialog.getPerson().getFaction());
		security.add(new CustomOption("(Go Quietly)", m -> {
			// TODO: different behavior
			m.select(def);
		}));
		menu.setDefault(new DialogOption("(Talk to Security)", security));

		dialog.add(new QuicktimeOption(3, "(Run Away)", def::onSelect));
	}
}
