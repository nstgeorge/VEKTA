package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.module.Module;

import static vekta.Vekta.getInstance;

public class ShipModuleOption implements MenuOption {
	private final LoadoutMenuOption parent;
	private final Module module;
	private final ModuleStatus status;

	public ShipModuleOption(LoadoutMenuOption parent, Module module) {
		this.parent = parent;
		this.module = module;

		Module best = parent.getUpgradeable().getModule(module.getType());
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
		parent.getUpgradeable().upgrade(module);
		parent.updateMenu(menu);
	}

	private enum ModuleStatus {
		BETTER("[^]", getInstance().color(255, 255, 0)),
		DIFFERENT("[*]", getInstance().color(200)),
		WORSE("", getInstance().color(100));

		private final String tag;
		private final int color;

		ModuleStatus(String tag, int color) {
			this.tag = tag;
			this.color = color;
		}
	}
}
