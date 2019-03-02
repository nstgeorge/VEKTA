package vekta.terrain;

import processing.core.PVector;
import vekta.Vekta;
import vekta.object.Ship;
import vekta.object.SpaceObject;

import static vekta.Vekta.*;

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

	public boolean land(Ship ship) {
		if(landed != null) {
			return false;
		}

		landed = ship;
		Vekta.removeObject(ship);
		
		ship.onLand(this);
		return true;
	}

	public boolean takeoff() {
		if(landed == null) {
			return false;
		}

		PVector offset = landed.getPosition().copy().sub(getParent().getPosition());
		landed.setVelocity(offset.setMag(getLaunchSpeed()).add(getParent().getVelocity()));
		landed.update(); // Boost the ship away from the planet
		Vekta.addObject(landed);
		landed.onDepart(getParent());
		landed = null;
		return true;
	}

	/**
	 * Compute the launch speed (escape velocity)
	 */
	private float getLaunchSpeed() {
		return (float)Math.sqrt((2 * G * (parent.getMass() / SCALE)) / (parent.getRadius() * SCALE));
	}
}
