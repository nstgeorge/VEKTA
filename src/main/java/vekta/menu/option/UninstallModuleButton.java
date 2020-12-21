package vekta.menu.option;

import vekta.menu.Menu;
import vekta.module.Module;
import vekta.module.ModuleUpgrader;

public class UninstallModuleButton extends ButtonOption {
	private final ModuleUpgrader upgrader;
	private final Module module;

	public UninstallModuleButton(ModuleUpgrader upgrader, Module module) {
		this.upgrader = upgrader;
		this.module = module;
	}

	@Override
	public String getName() {
		return "Uninstall " + module.getName();
	}

	@Override
	public String getSelectVerb() {
		return "uninstall";
	}

	@Override
	public void onSelect(Menu menu) {
		upgrader.uninstallModule(module);
	}
}
