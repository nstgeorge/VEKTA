package vekta.overlay.singleplayer;

import vekta.object.SpaceObject;
import vekta.object.ship.PlayerShip;
import vekta.overlay.Overlay;
import vekta.overlay.indicator.Indicator;
import vekta.overlay.indicator.OffScreenIndicator;
import vekta.overlay.indicator.OnScreenIndicator;

import java.util.ArrayList;

public class ObjectIndicatorOverlay implements Overlay {

	private ArrayList<Indicator> indicators;
	private PlayerShip ship;

	public ObjectIndicatorOverlay(PlayerShip ship) {
		this.ship = ship;

		indicators = new ArrayList<>();

		// Set up the target indicator
		// TODO: Create a dynamic indicator type that changes between off and on screen indicators
		indicators.add(new OffScreenIndicator(t -> ship.findNavigationTarget(), ship));
		indicators.add(new OnScreenIndicator(t -> ship.findNavigationTarget(), ship));
	}

	@Override
	public void render() {
		for(Indicator indicator : indicators) {
			indicator.draw();
		}
	}

	public void addIndicator(SpaceObject target) {
		boolean exists = false;

		// Check for duplicates
		for(Indicator indicator : indicators) {
			if(indicator instanceof OffScreenIndicator) {
				if(((OffScreenIndicator)indicator).getTarget() == target) {
					exists = true;
				}
			}
		}

		if(!exists) {
			indicators.add(new OffScreenIndicator(target, ship));
		}
	}

	public void removeIndicator(SpaceObject target) {
		for(Indicator indicator : indicators) {
			if(indicator instanceof OffScreenIndicator) {
				if(((OffScreenIndicator)indicator).getTarget() == target) {
					indicators.remove(indicator);
					return;
				}
			}
		}
	}
}
