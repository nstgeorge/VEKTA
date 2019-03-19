package vekta.spawner.dialog;

import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.option.BackOption;
import vekta.menu.option.CustomOption;
import vekta.menu.option.MenuOption;
import vekta.person.Dialog;
import vekta.person.Person;
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

	public static Dialog randomSecurityDialog(Faction faction, MenuOption escape) {
		Person guard = new Person("Security Guard", faction);
		Dialog dialog = guard.createDialog("security");
		dialog.add(new CustomOption("(Go Quietly)", m -> {
			// TODO: different behavior
			m.select(escape);
		}));
		return dialog;
	}
}
