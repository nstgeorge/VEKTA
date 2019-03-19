package vekta.menu.option;

import vekta.item.WeaponItem;
import vekta.menu.Menu;
import vekta.person.Person;
import vekta.spawner.dialog.SecurityDialogSpawner;

public class MurderOption implements MenuOption {
	private final Person person;
	private final WeaponItem weapon;

	public MurderOption(Person person, WeaponItem weapon) {
		this.person = person;
		this.weapon = weapon;
	}

	public Person getPerson() {
		return person;
	}

	public WeaponItem getWeapon() {
		return weapon;
	}

	@Override
	public String getName() {
		return getWeapon().getAction();
	}

	@Override
	public void onSelect(Menu menu) {
		MenuOption confront = new DialogOption("Confront Security",
				SecurityDialogSpawner.randomSecurityDialog(getPerson().getFaction(), menu.getDefault()));

		getPerson().die();

		menu.clear();
		menu.setDefault(confront);
		menu.add(new QuicktimeOption(3, "Leave", menu.getDefault()::onSelect));
	}
}
