package vekta.overlay.singleplayer;

import vekta.object.ship.PlayerShip;

/**
 * Ship stats (energy, money, etc.)
 */
public class ShipMoneyOverlay extends ShipStatOverlay {
	public ShipMoneyOverlay(int x, int y, PlayerShip ship) {
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
