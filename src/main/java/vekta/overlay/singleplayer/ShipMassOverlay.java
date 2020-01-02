package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;

import static processing.core.PApplet.round;

public class ShipMassOverlay extends ShipStatOverlay {
	public ShipMassOverlay(int x, int y, ModularShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Mass";
	}

	//	@Override
	//	public int getColor() {
	//		float ratio = getShip().getMass() / getShip().getBaseMass() - 1;
	//		return v.lerpColor(UI_COLOR, DANGER_COLOR, ratio);
	//	}

	@Override
	public String getValue() {
		return round(getShip().getMass() /*- getShip().getBaseMass()*/) + " kg";
	}
}
