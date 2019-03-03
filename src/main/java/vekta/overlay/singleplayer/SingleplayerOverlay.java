package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;
import vekta.overlay.CompoundOverlay;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
import static vekta.Vekta.bodyFont;
import static vekta.Vekta.v;

public class SingleplayerOverlay extends CompoundOverlay {
	public SingleplayerOverlay(PlayerShip ship) {
		super(new Overlay[] {
				new TelemetryOverlay(ship),
				new ShipComputerOverlay(50, -150, ship),
				new ShipEnergyOverlay(-300, -100 + 24, ship),
				new ShipMoneyOverlay(-300, -100 + 48, ship),
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
