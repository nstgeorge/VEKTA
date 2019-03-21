package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

public class ShipSwitchOption implements MenuOption {
	private final ModularShip ship;

	public ShipSwitchOption(ModularShip ship) {
		this.ship = ship;
	}

	@Override
	public String getName() {
		return "Switch to Vessel";
	}

	@Override
	public void onSelect(Menu menu) {
		ship.setController(menu.getPlayer());
		menu.close();
	}
}
