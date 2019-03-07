package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;

/**
 * Ship stats (energy, money, etc.)
 */
public class ShipTemperatureOverlay extends ShipStatOverlay {
	public ShipTemperatureOverlay(int x, int y, ModularShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Temperature";
	}

	@Override
	public String getValue() {
		return getShip().getTemperature() + " Celsius";
	}

	@Override
	public int getColor() {
//		float ratio = getShip().getEnergy() / getShip().getMaxEnergy();
//		if(ratio <= .1F) {
//			return v.color(255, 0, 0);
//		}
//		else if(ratio <= .2F) {
//			return v.color(255, 255, 0);
//		}
		return super.getColor();
	}
}
