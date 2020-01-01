package vekta.world;

import vekta.player.Player;

import java.io.Serializable;

public interface ZoomController extends Serializable {
	boolean shouldCancelZoomControl(Player player);

	float controlZoom(Player player, float zoom);
}
