package vekta.menu.option;

import vekta.menu.Menu;
import vekta.module.Module;
import vekta.module.ModuleUpgrader;

public class UninstallModuleOption implements MenuOption {
	private final ModuleUpgrader upgrader;
	private final Module module;

	public UninstallModuleOption(ModuleUpgrader upgrader, Module module) {
		this.upgrader = upgrader;
		this.module = module;
	}

	@Override
	public String getName() {
		return "Uninstall " + module.getName();
	}

	@Override
	public void onSelect(Menu menu) {
		upgrader.uninstallModule(module);
	}
}
