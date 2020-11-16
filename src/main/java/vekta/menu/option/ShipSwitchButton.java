package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

public class ShipSwitchButton extends ButtonOption {
	private final ModularShip ship;

	public ShipSwitchButton(ModularShip ship) {
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
