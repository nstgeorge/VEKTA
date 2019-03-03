package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.ModularShip;

public class RechargeOption implements MenuOption {
	private final ModularShip ship;

	public RechargeOption(ModularShip ship) {
		this.ship = ship;
	}

	@Override
	public String getName() {
		return "Recharge";
	}

	@Override
	public void select(Menu menu) {
		ship.recharge();
		menu.remove(this);
	}
}
