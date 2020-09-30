package vekta.world;

import vekta.player.Player;

public class AttributeZoomController implements ZoomController {
	private final Class<? extends Player.Attribute> type;
	private final float maxZoom;

	public AttributeZoomController(Class<? extends Player.Attribute> type, float maxZoom) {
		this.type = type;
		this.maxZoom = maxZoom;
	}

	@Override
	public boolean shouldCancelZoomControl(Player player) {
		return !player.hasAttribute(type);
	}

	@Override
	public float controlZoom(Player player, float zoom) {
		if(zoom > maxZoom) {
			zoom = maxZoom;
		}
		return zoom;
	}
}
