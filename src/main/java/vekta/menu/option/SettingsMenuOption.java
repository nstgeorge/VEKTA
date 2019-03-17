package vekta.menu.option;

import vekta.context.SettingsMenuContext;
import vekta.menu.Menu;

import static vekta.Vekta.setContext;

public class SettingsMenuOption implements MenuOption {

	@Override
	public String getName() {
		return "Settings";
	}

	@Override
	public void onSelect(Menu menu) {
		setContext(new SettingsMenuContext(menu));
	}
}
