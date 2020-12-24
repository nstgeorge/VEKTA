package vekta.world;

import vekta.object.SpaceObject;
import vekta.player.Player;

public class LandingZoomController implements ZoomController {

	private final SpaceObject target;

	public LandingZoomController(SpaceObject target) {
		this.target = target;
	}

	@Override
	public boolean shouldCancelZoomControl(Player player) {
		return true;/////
	}

	@Override
	public float controlZoom(Player player, float zoom) {
		return zoom;
	}
}
