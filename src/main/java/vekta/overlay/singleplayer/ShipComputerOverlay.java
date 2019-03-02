package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;
import vekta.object.module.TargetingModule;
import vekta.overlay.TextOverlay;

import static vekta.Vekta.UI_COLOR;

/**
 * Ship computer (autopilot, targeter, etc.)
 * */
public class ShipComputerOverlay extends TextOverlay {
	private final PlayerShip ship;

	public ShipComputerOverlay(int x, int y, PlayerShip ship) {
		super(x, y);
		
		this.ship = ship;
	}

	@Override public String getText() {
		if(ship.isLanding()) {
			return ":: Autopilot: Landing... ::";
		}
		else if(TargetingModule.isUsingTargeter()) {
			return ":: Targeting Computer: planet [1], ship [2] ::";
		}
		return null;
	}

	@Override
	public int getColor() {
		return UI_COLOR;
	}
}
