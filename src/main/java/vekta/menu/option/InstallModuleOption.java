package vekta.menu.option;

import vekta.module.ModuleUpgrader;
import vekta.menu.Menu;
import vekta.module.Module;

import static vekta.Vekta.v;

public class InstallModuleOption implements MenuOption {
	private final ModuleUpgrader upgrader;
	private final Module module;
	private final ModuleStatus status;

	public InstallModuleOption(ModuleUpgrader upgrader, Module module) {
		this.upgrader = upgrader;
		this.module = module;

		Module best = upgrader.getRelevantModule(module);
		if(best == null || module.isBetter(best)) {
			status = ModuleStatus.BETTER;
		}
		else if(best.isBetter(module)) {
			status = ModuleStatus.WORSE;
		}
		else {
			status = ModuleStatus.DIFFERENT;
		}
	}

	@Override
	public String getName() {
		return module.getName() + status.tag;
	}

	@Override
	public int getColor() {
		return status.color;
	}

	@Override
	public void select(Menu menu) {
		upgrader.installModule(module);
	}

	private enum ModuleStatus {
		BETTER("[^]", v.color(255, 255, 0)),
		DIFFERENT("[*]", v.color(200)),
		WORSE("[-]", v.color(100));

		private final String tag;
		private final int color;

		ModuleStatus(String tag, int color) {
			this.tag = tag;
			this.color = color;
		}
	}
}
