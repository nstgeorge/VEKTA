package vekta.terrain;

import processing.core.PVector;
import vekta.sync.Syncable;
import vekta.knowledge.ObservationLevel;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;
import vekta.sound.Tune;
import vekta.spawner.TuneGenerator;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.Settlement;

import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

/**
 * A landing site for one spacecraft-like object.
 */
public class LandingSite extends Syncable<LandingSite> {
	private static final float LAUNCH_SPEED_SCALE = .5F;

	private final Location location;

	private Ship landed;

	public LandingSite(Location location) {
		this.location = location;
	}

	public SpaceObject getParent() {
		return getLocation().getPlanet();
	}

	public Location getLocation() {
		return location;
	}

	public Ship getLanded() {
		return landed;
	}

	public void land(Ship ship) {
		if(landed != null) {
			takeoff();
		}

		landed = ship;
		getWorld().remove(ship);

		// Set position/velocity for takeoff
		PVector offset = landed.getPosition().copy().sub(getParent().getPosition());
		PVector velocity = offset.setMag(getLaunchSpeed()).add(getParent().getVelocity());
		landed.setVelocity(velocity);
		//		landed.getPositionReference().add(velocity.mult(getWorld().getTimeScale()));

		ship.setTemperatureKelvin(ship.getOptimalTemperature());
		ship.doLand(this);
	}

	public void takeoff() {
		if(landed == null) {
			return;
		}

		register(landed);
		landed.undock(); // Start landing debounce
		landed.setHeading(getParent().relativePosition(landed));
		landed.onDepart(getParent());
		landed = null;
	}

	/**
	 * Compute the launch speed
	 */
	private float getLaunchSpeed() {
		return (float)Math.sqrt(2 * G * getParent().getMass() / getParent().getRadius()) * LAUNCH_SPEED_SCALE;
	}
}
