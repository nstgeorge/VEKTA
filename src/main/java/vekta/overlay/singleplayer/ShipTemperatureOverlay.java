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
	public String getText() {
		return getValue();
	}

	@Override
	public String getValue() {
		if(isInDanger()) {
			return "DANGER: " + (float)Math.round(getShip().getTemperatureKelvin() * 10) / 10 + " K";
		}
		return "";
	}

	@Override
	public int getColor() {
		float flash = (float)((.1 * Math.sin((v.millis()) / 100F)) + 0.9);
		if(isInDanger()) {
			return v.color(255, 0, 0, 255 * flash);
		}
		return super.getColor();
	}

	public boolean isInDanger() {
		float temp = getShip().getTemperatureKelvin();
		return temp < 0 || temp > 40;
	}
}
