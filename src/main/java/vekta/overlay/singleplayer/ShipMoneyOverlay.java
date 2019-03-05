package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;

/**
 * Ship stats (energy, money, etc.)
 */
public class ShipMoneyOverlay extends ShipStatOverlay {
	public ShipMoneyOverlay(int x, int y, ModularShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Gold";
	}

	@Override
	public String getValue() {
		return String.valueOf(getShip().getInventory().getMoney());
	}
}
