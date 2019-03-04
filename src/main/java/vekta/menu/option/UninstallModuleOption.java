package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.module.Module;
import vekta.object.module.Upgrader;

public class UninstallModuleOption implements MenuOption {
	private final Upgrader upgrader;
	private final Module module;

	public UninstallModuleOption(Upgrader upgrader, Module module) {
		this.upgrader = upgrader;
		this.module = module;
	}

	@Override
	public String getName() {
		return "Uninstall " + module.getName();
	}

	@Override
	public void select(Menu menu) {
		upgrader.removeModule(module);
	}
}
