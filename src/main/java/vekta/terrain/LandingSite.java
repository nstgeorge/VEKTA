package vekta.terrain;

import processing.core.PVector;
import vekta.Vekta;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;

import static vekta.Vekta.G;
import static vekta.Vekta.getWorld;

/**
 * A landing terrain for one spacecraft-like object.
 * State management for landing sites should be handled or proxied through this class.
 */
public class LandingSite {
	private final SpaceObject parent;
	private final Terrain terrain;

	private Ship landed;

	public LandingSite(SpaceObject parent, Terrain terrain) {
		this.parent = parent;
		this.terrain = terrain;
	}

	public SpaceObject getParent() {
		return parent;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Ship getLanded() {
		return landed;
	}

	public void land(Ship ship) {
		if(landed != null) {
			takeoff();
		}

		landed = ship;
		Vekta.removeObject(ship);

		ship.doLand(this);
	}

	public void takeoff() {
		if(landed == null) {
			return;
		}

		Vekta.addObject(landed);
		landed.onDepart(getParent());

		// Set position/velocity for takeoff
		PVector offset = landed.getPosition().copy().sub(getParent().getPosition());
		PVector velocity = offset.setMag(getLaunchSpeed()).add(getParent().getVelocity());
		landed.setVelocity(velocity);
		landed.applyVelocity(velocity); // Boost the ship away from the planet

		landed = null;
	}

	/**
	 * Compute the launch speed (escape velocity)
	 */
	private float getLaunchSpeed() {
		float escapeScale = 10; // Boost a bit more than escape velocity
		return escapeScale * (float)Math.sqrt(2 * G * parent.getMass() / parent.getRadius() / getWorld().getTimeScale());
	}
}
