package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;

import static vekta.Vekta.v;

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
		float temp = getShip().getTemperature();
		if(temp < 0 || temp > 40) {
			return v.color(255, 0, 0);
		}
		return super.getColor();
	}
}
