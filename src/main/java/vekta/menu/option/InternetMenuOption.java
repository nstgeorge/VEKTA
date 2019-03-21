package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.InternetMenuHandle;

import static vekta.Vekta.setContext;

public class InternetMenuOption implements MenuOption {
	private final boolean connected;

	public InternetMenuOption(boolean connected) {
		this.connected = connected;
	}

	@Override
	public String getName() {
		return connected ? "Internet" : "(No Internet Relay)";
	}

	@Override
	public boolean isEnabled() {
		return connected;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new InternetMenuHandle());

		sub.add(new BondMenuOption());

		sub.addDefault();
		setContext(sub);
	}
}
