package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import static vekta.Vekta.setContext;

public class InternetMenuOption implements MenuOption {
	private final Player player;
	private final boolean enabled;

	public InternetMenuOption(Player player, boolean enabled) {
		this.player = player;
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
		Menu sub = new Menu(menu, new MenuHandle());

		sub.add(new BondMenuOption());

		sub.addDefault();
		setContext(sub);
	}
}
