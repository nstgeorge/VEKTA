package vekta.object;

import processing.core.PVector;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	private final LandingSite site;
	private final Terrain terrain;

	public TerrestrialPlanet(float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(mass, density, position, velocity, color);

		this.terrain = terrain;
		this.site = new LandingSite(this, terrain);
	}

	public LandingSite getLandingSite() {
		return site;
	}

	@Override
	public boolean isHabitable() {
		return terrain.has("Habitable");
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof Ship) {
			Ship ship = (Ship)s;
			if(ship.isLanding()) {
				site.land(ship);
				return; // Prevent ships from being destroyed after landing
			}
		}
		super.onCollide(s);
	}

	@Override
	public void onDestroy(SpaceObject s) {
		super.onDestroy(s);

		// If something landed on this planet, destroy it as well
		SpaceObject landed = site.getLanded();
		if(landed != null) {
			landed.onDestroy(s);
		}
	}
}
