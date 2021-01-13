package vekta.overlay.singleplayer;

import vekta.object.SpaceObject;
import vekta.object.ship.PlayerShip;
import vekta.overlay.Overlay;
import vekta.overlay.indicator.Indicator;
import vekta.overlay.indicator.OffScreenIndicator;
import vekta.overlay.indicator.OnScreenIndicator;
import vekta.player.Player;

import java.util.ArrayList;

public class IndicatorOverlay implements Overlay {

	private final ArrayList<Indicator> indicators;
	private final Player player;

	public IndicatorOverlay(Player player) {
		this.player = player;

		indicators = new ArrayList<>();

		// Set up the target indicator
		// TODO: Create a dynamic indicator type that changes between off and on screen indicators
		indicators.add(new OffScreenIndicator(() -> player.getShip().findNavigationTarget(), player));
		indicators.add(new OnScreenIndicator(() ->  player.getShip().findNavigationTarget(), player));
	}

	@Override
	public void render() {
		for(Indicator indicator : indicators) {
			indicator.draw();
		}
	}

	/// Currently unused ///

	//	public void addIndicator(SpaceObject target) {
	//		boolean exists = false;
	//
	//		// Check for duplicates
	//		for(Indicator indicator : indicators) {
	//			if(indicator instanceof OffScreenIndicator) {
	//				if(((OffScreenIndicator)indicator).getTarget() == target) {
	//					exists = true;
	//				}
	//			}
	//		}
	//
	//		if(!exists) {
	//			indicators.add(new OffScreenIndicator(target, ship));
	//		}
	//	}
	//
	//	public void removeIndicator(SpaceObject target) {
	//		for(Indicator indicator : indicators) {
	//			if(indicator instanceof OffScreenIndicator) {
	//				if(((OffScreenIndicator)indicator).getTarget() == target) {
	//					indicators.remove(indicator);
	//					return;
	//				}
	//			}
	//		}
	//	}
}
