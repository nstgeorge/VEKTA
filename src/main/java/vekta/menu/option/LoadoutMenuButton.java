package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.module.Module;
import vekta.module.ModuleUpgradeable;
import vekta.module.ModuleUpgrader;

import static vekta.Vekta.setContext;

public class LoadoutMenuButton implements ButtonOption, ModuleUpgrader {
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
		Menu sub = new Menu(menu.getPlayer(), menu.getDefault(), new LoadoutMenuHandle(upgradeable.getModules()));
		sub.addSelectListener(option -> updateMenu(sub));
		updateMenu(sub);
		setContext(sub);
	}

	@Override
	public Module getRelevantModule(Module module) {
		return getUpgradeable().getModule(module.getType());
	}

	@Override
	public void installModule(Module module) {
		getUpgradeable().addModule(module);
	}

	@Override
	public void uninstallModule(Module module) {
		getUpgradeable().removeModule(module);
	}

	private void updateMenu(Menu sub) {
		sub.clear();
		for(Module module : upgradeable.findUpgrades()) {
			sub.add(new InstallModuleButton(this, module));
		}
		sub.addDefault();
	}
}
