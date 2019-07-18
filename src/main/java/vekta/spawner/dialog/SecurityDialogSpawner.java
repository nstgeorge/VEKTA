package vekta.spawner.dialog;

import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.option.BackButton;
import vekta.menu.option.CustomButton;
import vekta.menu.option.MenuOption;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.DialogGenerator;

public class SecurityDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "security";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		menu.setDefault(new BackButton(menu));
	}

	public static Dialog randomSecurityDialog(Faction faction, MenuOption escape) {
		Person guard = new TemporaryPerson("Security Guard", faction);
		Dialog dialog = guard.createDialog("security");
		dialog.add(new CustomButton("Go Quietly", m -> {
			// TODO: different behavior
			m.select(escape);
		}));
		return dialog;
	}
}
