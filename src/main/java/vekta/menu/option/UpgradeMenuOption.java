package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.object.module.Module;
import vekta.object.module.Upgradeable;

import java.util.List;

import static vekta.Vekta.setContext;

public class UpgradeMenuOption implements MenuOption {
	private final Upgradeable upgradeable;
	private final List<Module> modules;

	public UpgradeMenuOption(Upgradeable upgradeable, List<Module> modules) {
		this.upgradeable = upgradeable;
		this.modules = modules;
	}

	@Override
	public String getName() {
		return "Upgrade";
	}

	@Override
	public void select(Menu menu) {
		MenuOption def = new BackOption(menu);
		Menu sub = new Menu(new MenuHandle(def));
		for(Module module : modules) {
			sub.add(new UpgradeOption(upgradeable, module));
		}
		sub.add(def);
		setContext(sub);
	}
}
