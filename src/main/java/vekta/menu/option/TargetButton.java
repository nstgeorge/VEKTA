package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.SpaceObject;

public class TargetButton extends ButtonOption {
	private final SpaceObject target;

	public TargetButton(SpaceObject target) {
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
