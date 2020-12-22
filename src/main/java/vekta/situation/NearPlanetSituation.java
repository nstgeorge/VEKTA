package vekta.situation;

import vekta.Settings;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.world.AttributeMaxZoomController;
import vekta.world.RenderLevel;

import static processing.core.PApplet.max;
import static vekta.Vekta.getDistanceUnit;
import static vekta.Vekta.getWorld;

public class NearPlanetSituation implements Situation {

	private static final float NEAR_PLANET_TRESHOLD = 1E10F;    // How close a player can be before they are considered "near" a planet

	@Override
	public boolean isHappening(Player player) {
		for(TerrestrialPlanet planet : getWorld().findObjects(TerrestrialPlanet.class)) {
			if(player.getShip().relativePosition(planet).mag() < NEAR_PLANET_TRESHOLD) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void start(Player player) {
		if(Settings.getBoolean("zoomNearPlanets")) {
			//		getWorld().addZoomController(new AttributeMaxZoomController(getClass(), getDistanceUnit(RenderLevel.PLANET)));
			float maxZoom = getDistanceUnit(RenderLevel.PLANET);
			if(getWorld().getZoom() > maxZoom) {
				getWorld().setZoom(maxZoom);
			}
		}
	}
}
