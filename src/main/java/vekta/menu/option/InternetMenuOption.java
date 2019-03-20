package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.InternetMenuHandle;

import static vekta.Vekta.setContext;

public class InternetMenuOption implements MenuOption {
	private final boolean enabled;

	public InternetMenuOption(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getName() {
		return "Internet";
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new InternetMenuHandle());

		sub.add(new BondMenuOption());

		sub.addDefault();
		setContext(sub);
	}
}
