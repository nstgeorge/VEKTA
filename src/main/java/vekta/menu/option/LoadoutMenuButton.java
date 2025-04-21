package vekta.menu.option;

import vekta.context.StationLayoutContext;
import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.module.BaseModule;
import vekta.module.ModuleUpgradeable;
import vekta.module.ModuleUpgrader;
import vekta.object.ship.SpaceStation;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class LoadoutMenuButton extends ButtonOption implements ModuleUpgrader {
	private final ModuleUpgradeable upgradeable;

	public LoadoutMenuButton(ModuleUpgradeable upgradeable) {
		this.upgradeable = upgradeable;
	}

	public ModuleUpgradeable getUpgradeable() {
		return upgradeable;
	}

	@Override
	public String getName() {
		return "Loadout";
	}

	@Override
	public void onSelect(Menu menu) {
		// TEMP
		if (upgradeable instanceof SpaceStation) {
			setContext(new StationLayoutContext(getWorld(), (SpaceStation) upgradeable, menu.getPlayer()));
			return;
		}

		Menu sub = new Menu(menu, new LoadoutMenuHandle(upgradeable.getModules(), true));
		sub.addSelectListener(option -> updateMenu(sub));
		updateMenu(sub);
		setContext(sub);
	}

	@Override
	public BaseModule getRelevantModule(BaseModule module) {
		return getUpgradeable().getModule(module.getType());
	}

	@Override
	public void installModule(BaseModule module) {
		getUpgradeable().addModule(module);
	}

	@Override
	public void uninstallModule(BaseModule module) {
		getUpgradeable().removeModule(module);
	}

	private void updateMenu(Menu sub) {
		sub.clear();
		for (BaseModule module : upgradeable.findUpgrades()) {
			sub.add(new InstallModuleButton(this, module));
		}
		sub.addDefault();
	}
}
