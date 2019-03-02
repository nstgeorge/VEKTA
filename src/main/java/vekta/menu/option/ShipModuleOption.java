package vekta.menu.option;

import vekta.Vekta;
import vekta.menu.Menu;
import vekta.object.module.Module;

import static vekta.Vekta.getInstance;

public class ShipModuleOption implements MenuOption {
	private final LoadoutMenuOption parent;
	private final Module module;
	private final boolean isBest;

	public ShipModuleOption(LoadoutMenuOption parent, Module module) {
		this.parent = parent;
		this.module = module;

		Module best = parent.getUpgradeable().getBestModule(module.getType());
		isBest = best == null || module.isBetter(best);
	}

	@Override
	public String getName() {
		return module.getName() + (isBest ? " [^]" : "");
	}

	@Override
	public int getColor() {
		Vekta v = getInstance();
		return isBest ?v.color(255, 255, 0) : MenuOption.super.getColor();
	}

	@Override
	public void select(Menu menu) {
		parent.getUpgradeable().upgrade(module);
		parent.updateMenu(menu);
	}
}
