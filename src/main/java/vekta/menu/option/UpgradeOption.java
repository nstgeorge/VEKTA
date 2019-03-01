package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.module.Module;
import vekta.object.module.Upgradeable;

public class UpgradeOption implements MenuOption {
	private final Upgradeable upgradeable;
	private final Module module;

	public UpgradeOption(Upgradeable upgradeable, Module module) {
		this.upgradeable = upgradeable;
		this.module = module;
	}

	@Override
	public String getName() {
		return module.getName();
	}

	@Override
	public void select(Menu menu) {
		upgradeable.upgrade(module);
		menu.remove(this);
		menu.scroll(-1);
	}
}
