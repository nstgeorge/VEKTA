package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;

/**
 * Ship stats (energy, money, etc.)
 */
public class ShipEnergyOverlay extends ShipStatOverlay {
	public ShipEnergyOverlay(int x, int y, PlayerShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Energy";
	}

	@Override
	public String getValue() {
		float energy = getShip().getEnergy();
		float max = getShip().getMaxEnergy();
		return (int)energy + " / " + max + " (" + (int)(energy / max * 100) + "%)";
	}
}
