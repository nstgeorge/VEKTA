package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;

public class ShipMassOverlay extends ShipStatOverlay {
	public ShipMassOverlay(int x, int y, ModularShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Mass";
	}

	@Override
	public String getValue() {
		return Math.round(getShip().getMass()) + " kg";
	}
}
