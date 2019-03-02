package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.object.module.Module;
import vekta.object.module.Upgradeable;

import static vekta.Vekta.setContext;

public class LoadoutMenuOption implements MenuOption {
	private final Upgradeable upgradeable;

	public LoadoutMenuOption(Upgradeable upgradeable) {
		this.upgradeable = upgradeable;
	}

	public Upgradeable getUpgradeable() {
		return upgradeable;
	}

	@Override
	public String getName() {
		return "Loadout";
	}

	@Override
	public void select(Menu menu) {
		Menu sub = new Menu(new LoadoutMenuHandle(new BackOption(menu), upgradeable));
		updateMenu(sub);
		setContext(sub);
	}

	public void updateMenu(Menu sub) {
		sub.clear();
		for(Module module : upgradeable.findUpgrades()) {
			sub.add(new ShipModuleOption(this, module));
		}
		sub.addDefault();
	}
}
