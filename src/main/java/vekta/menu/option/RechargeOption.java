package vekta.menu.option;

import vekta.menu.Menu;
import vekta.object.PlayerShip;

public class RechargeOption implements MenuOption {
	private final PlayerShip ship;

	public RechargeOption(PlayerShip ship) {
		this.ship = ship;
	}

	@Override
	public String getName() {
		return "Recharge";
	}

	@Override
	public void select(Menu menu) {
		ship.recharge();
	}
}
