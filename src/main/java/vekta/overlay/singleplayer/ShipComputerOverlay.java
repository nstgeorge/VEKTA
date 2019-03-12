package vekta.overlay.singleplayer;

import vekta.object.ship.ModularShip;
import vekta.overlay.TextOverlay;

import static vekta.Vekta.UI_COLOR;

/**
 * Ship computer (autopilot, targeter, etc.)
 */
public class ShipComputerOverlay extends TextOverlay {
	private final ModularShip ship;

	public ShipComputerOverlay(int x, int y, ModularShip ship) {
		super(x, y);

		this.ship = ship;
	}

	@Override
	public String getText() {
		if(!ship.hasEnergy()) {
			String reason = ship.isOverheated() ? "Overheated!" : "Emergy Power";
			return ":: Override: " + reason + " ::";
		}
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
