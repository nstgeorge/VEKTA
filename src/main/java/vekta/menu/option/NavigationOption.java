package vekta.menu.option;

import vekta.context.NavigationContext;
import vekta.menu.Menu;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class NavigationOption implements MenuOption {
	public NavigationOption() {
	}

	@Override
	public String getName() {
		return "Navigation";
	}

	@Override
	public void onSelect(Menu menu) {
		setContext(new NavigationContext(getWorld(), menu.getPlayer()));
	}
}
