package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.object.module.Module;
import vekta.object.module.Upgradeable;

import static vekta.Vekta.setContext;

public class UpgradeMenuOption implements MenuOption {
	private final Upgradeable upgradeable;

	public UpgradeMenuOption(Upgradeable upgradeable) {
		this.upgradeable = upgradeable;
	}

	@Override
	public String getName() {
		return "Upgrade";
	}

	@Override
	public void select(Menu menu) {
		MenuOption def = new BackOption(menu);
		Menu sub = new Menu(new MenuHandle(def));
		for(Module module : upgradeable.findUpgrades()) {
			sub.add(new UpgradeOption(upgradeable, module));
		}
		sub.add(def);
		setContext(sub);
	}
}
