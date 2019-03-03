package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;
import vekta.overlay.TextOverlay;

import static vekta.Vekta.UI_COLOR;

/**
 * Ship computer (autopilot, targeter, etc.)
 */
public class ShipComputerOverlay extends TextOverlay {
	private final PlayerShip ship;

	public ShipComputerOverlay(int x, int y, PlayerShip ship) {
		super(x, y);

		this.ship = ship;
	}

	@Override
	public String getText() {
		if(ship.isLanding()) {
			return ":: Autopilot: Engaged ::"; // TODO: split into two Overlays, one for landing/docking mode and the other for autopilot messages
		}
		return null;
	}

	@Override
	public int getColor() {
		return UI_COLOR;
	}
}
