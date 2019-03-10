package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.module.Module;
import vekta.module.ModuleUpgradeable;
import vekta.module.ModuleUpgrader;

import static vekta.Vekta.setContext;

public class LoadoutMenuOption implements MenuOption, ModuleUpgrader {
	private final ModuleUpgradeable upgradeable;

	private Menu subMenu;

	public LoadoutMenuOption(ModuleUpgradeable upgradeable) {
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
	public void select(Menu menu) {
		subMenu = new Menu(menu.getPlayer(), new LoadoutMenuHandle(menu.getDefault(), upgradeable.getModules()));
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
