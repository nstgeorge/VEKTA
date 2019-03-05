package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.object.ship.ModularShip;
import vekta.module.Module;
import vekta.module.Upgradeable;
import vekta.module.Upgrader;

import static vekta.Vekta.setContext;

public class LoadoutMenuOption implements MenuOption, Upgrader {
	private final ModularShip upgradeable;

	private Menu subMenu;

	public LoadoutMenuOption(ModularShip upgradeable) {
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
		subMenu = new Menu(new LoadoutMenuHandle(new BackOption(menu), upgradeable.getModules()));
		updateMenu();
		setContext(subMenu);
	}

	@Override
	public Module getRelevantModule(Module module) {
		return getUpgradeable().getModule(module.getType());
	}

	@Override
	public void installModule(Module module) {
		getUpgradeable().addModule(module);
		updateMenu();
	}

	@Override
	public void uninstallModule(Module module) {
		getUpgradeable().removeModule(module);
		updateMenu();
	}

	private void updateMenu() {
		subMenu.clear();
		for(Module module : upgradeable.findUpgrades()) {
			subMenu.add(new InstallModuleOption(this, module));
		}
		subMenu.addDefault();
	}
}
