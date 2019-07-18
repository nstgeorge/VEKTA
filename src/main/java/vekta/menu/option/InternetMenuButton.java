package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.InternetMenuHandle;

import static vekta.Vekta.setContext;

public class InternetMenuButton implements ButtonOption {
	private final boolean connected;

	public InternetMenuButton(boolean connected) {
		this.connected = connected;
	}

	@Override
	public String getName() {
		return connected ? "Internet" : "(Too Remote for Internet)";
	}

	@Override
	public boolean isEnabled() {
		return connected;
	}

	@Override
	public String getSelectVerb() {
		return "connect";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new InternetMenuHandle());

		sub.add(new BondMenuButton());

		sub.addDefault();
		setContext(sub);
	}
}
