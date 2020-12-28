package vekta.overlay.singleplayer;

import vekta.object.SpaceObject;
import vekta.object.ship.PlayerShip;
import vekta.overlay.Overlay;
import vekta.overlay.indicator.OffScreenIndicator;

import java.util.ArrayList;

public class ObjectIndicatorOverlay implements Overlay {

	private ArrayList<OffScreenIndicator> indicators;
	private PlayerShip ship;

	public ObjectIndicatorOverlay(PlayerShip ship) {
		this.ship = ship;

		indicators = new ArrayList<>();

		// Set up the target indicator
		indicators.add(new OffScreenIndicator(t -> ship.findNavigationTarget(), ship));
	}

	@Override
	public void render() {
		for(OffScreenIndicator indicator : indicators) {
			indicator.draw();
		}
	}

	public void addIndicator(SpaceObject target) {
		boolean exists = false;

		// Check for duplicates
		for(OffScreenIndicator indicator : indicators) {
			if(indicator.getTarget() == target) {
				exists = true;
			}
		}

		if(!exists) {
			indicators.add(new OffScreenIndicator(target, ship));
		}
	}

	public void removeIndicator(SpaceObject target) {
		for(OffScreenIndicator indicator : indicators) {
			if(indicator.getTarget() == target) {
				indicators.remove(indicator);
				return;
			}
		}
	}
}
