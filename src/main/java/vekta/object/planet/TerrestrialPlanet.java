package vekta.object.planet;

import processing.core.PVector;
import vekta.object.ship.ModularShip;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	private final LandingSite site;

	public TerrestrialPlanet(String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		this.site = new LandingSite(this, terrain);
	}

	public LandingSite getLandingSite() {
		return site;
	}

	public Terrain getTerrain() {
		return getLandingSite().getTerrain();
	}

	@Override
	public boolean isHabitable() {
		return getTerrain().hasFeature("Habitable");
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof ModularShip) {
			ModularShip ship = (ModularShip)s;
			if(ship.isLanding() && site.land(ship)) {
				return; // Prevent ships from being destroyed after landing
			}
		}
		super.onCollide(s); // Oof
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
