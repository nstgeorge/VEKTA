package vekta.object;

import processing.core.PVector;
import vekta.LandingSite;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	private final LandingSite site;
	private final boolean habitable;

	public TerrestrialPlanet(float mass, float density, boolean habitable, PVector position, PVector velocity, int color) {
		super(mass, density, position, velocity, color);

		this.habitable = habitable;
		this.site = new LandingSite(this);
	}

	public LandingSite getLandingSite() {
		return site;
	}

	@Override
	public boolean isHabitable() {
		return habitable;
	}

	@Override
	public void onCollide(SpaceObject s) {
		// Check if landing
		if(isHabitable()/*TODO: custom menu for uninhabitable planets*/ && s instanceof PlayerShip) { // TODO: create `Lander` interface for event handling
			PlayerShip ship = (PlayerShip)s;
			if(ship.isLanding()) {
				if(site.land(ship)) {
					return;
				}
			}
		}
		// Oof
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
