package vekta.menu.option;

import vekta.menu.Menu;
import vekta.module.BaseModule;
import vekta.module.ModuleUpgrader;

public class UninstallModuleButton extends ButtonOption {
	private final ModuleUpgrader upgrader;
	private final BaseModule module;

	public UninstallModuleButton(ModuleUpgrader upgrader, BaseModule module) {
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
