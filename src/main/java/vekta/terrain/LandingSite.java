package vekta.terrain;

import processing.core.PVector;
import vekta.Syncable;
import vekta.knowledge.ObservationLevel;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;
import vekta.sound.Tune;
import vekta.spawner.TuneGenerator;

import static vekta.Vekta.*;

/**
 * A landing site for one spacecraft-like object.
 */
public class LandingSite extends Syncable<LandingSite> {
	private static final float LAUNCH_SPEED_SCALE = 2;

	private final SpaceObject parent;
	private final Terrain terrain;

	private final Tune tune = TuneGenerator.randomTune();

	private Ship landed;

	public LandingSite(SpaceObject parent, Terrain terrain) {
		this.parent = parent;
		this.terrain = terrain;

		terrain.setup(this);
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

	public Tune getTune() {
		return tune;
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

		ship.setTemperature(ship.getOptimalTemperature()); // TODO: adjust based on planet temperature
		ship.doLand(this);
	}

	public void takeoff() {
		if(landed == null) {
			return;
		}

		register(landed);
		landed.undock(); // Start landing debounce
		landed.onDepart(getParent());
		landed = null;
	}

	public void observe(ObservationLevel level) {

	}

	/**
	 * Compute the launch speed
	 */
	private float getLaunchSpeed() {
		return (float)Math.sqrt(2 * G * parent.getMass() / parent.getRadius()) * LAUNCH_SPEED_SCALE;
	}
}
