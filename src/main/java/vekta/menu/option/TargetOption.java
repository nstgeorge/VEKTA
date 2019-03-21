package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.SpaceObject;

public class TargetOption implements MenuOption {
	private final SpaceObject target;

	public TargetOption(SpaceObject target) {
		this.target = target;
	}

	@Override
	public String getName() {
		return "Set Target";
	}

	@Override
	public void onSelect(Menu menu) {
		menu.getPlayer().getShip().setNavigationTarget(target);
		menu.close();
	}
}
