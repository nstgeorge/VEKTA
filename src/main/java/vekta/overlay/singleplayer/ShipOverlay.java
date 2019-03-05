package vekta.overlay.singleplayer;

import vekta.object.ship.PlayerShip;
import vekta.overlay.CompoundOverlay;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
import static vekta.Vekta.bodyFont;
import static vekta.Vekta.v;

public class ShipOverlay extends CompoundOverlay {
	public ShipOverlay(PlayerShip ship) {
		super(new Overlay[] {
				new TelemetryOverlay(ship),
				new ShipComputerOverlay(50, -150, ship),
				new ShipEnergyOverlay(-300, -75, ship),
				new ShipMoneyOverlay(-300, -50, ship),
		});
	}

	@Override
	public void draw() {
		// Set overlay text settings
		v.textFont(bodyFont);
		v.textAlign(LEFT);
		v.textSize(16);

		super.draw();
	}
}
