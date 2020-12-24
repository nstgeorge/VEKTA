package vekta.situation;

import vekta.Resources;
import vekta.Settings;
import vekta.object.planet.BlackHole;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.world.AttributeMaxZoomController;
import vekta.world.RenderLevel;
import vekta.world.Singleplayer;

import static processing.core.PApplet.sq;
import static vekta.Vekta.getDistanceUnit;
import static vekta.Vekta.getWorld;

public class AtmosphereSituation implements Situation {

	@Override
	public boolean isHappening(Player player) {
		for(TerrestrialPlanet planet : getWorld().findObjects(TerrestrialPlanet.class)) {
			if((player.getShip().relativePosition(planet).magSq() < sq(planet.getAtmosphereRadius()))) {
				// ((Singleplayer)getWorld()).setAngle(player.getShip().getPosition().sub(planet.getPosition()).heading());
//				System.out.println("Within atmosphere for " + planet.getName() + ": Distance = " + player.getShip().relativePosition(planet).mag() + ", Radius = " + planet.getAtmosphereRadius());
				return true;
			}
		}
		return false;
	}

	@Override
	public void start(Player player) {
		if(Settings.getBoolean("zoomNearPlanets")){
//			getWorld().addZoomController(new AttributeMaxZoomController(getClass(), getDistanceUnit(RenderLevel.ATMOSPHERE)));
			float maxZoom = getDistanceUnit(RenderLevel.ATMOSPHERE);
			if(getWorld().getZoom() > maxZoom) {
				getWorld().setZoom(maxZoom);
			}
		}
		Resources.setMusic("subatmosphere_0", true);
	}

	@Override
	public void end(Player player) {
		((Singleplayer)getWorld()).setAngle(0);
	}
}
