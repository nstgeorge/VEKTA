package vekta.object;

import processing.core.PVector;
import vekta.action.Action;
import vekta.action.runner.Runner;
import vekta.action.runner.RunnerState;
import vekta.knowledge.ObservationLevel;
import vekta.object.ship.ModularShip;
import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.sync.Syncable;
import vekta.world.RenderLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.*;

public abstract class SpaceObject extends Syncable<SpaceObject> implements Serializable {
	private static final float PREVIEW_ROTATE_SPEED = 5e-3F;
	private static final float MARKER_SIZE = 40;
	private static final int DEFAULT_TRAIL_LENGTH = 50;
	private static final float MOTION_SYNC_FACTOR = .2F; // How much to over/under-correct object motion

	protected final float[][] trail;

	private final List<Runner> startedRunners = new ArrayList<>();

	private boolean persistent;
	private boolean destroyed;

	protected final PVector position = new PVector();
	protected final PVector velocity = new PVector();
	private int color;

	private float aliveTime;
	private float temperature;

	public SpaceObject(PVector position, PVector velocity, int color) {
		this.position.set(position);
		this.velocity.set(velocity);
		this.color = color;

		this.trail = new float[getTrailLength()][];
	}

	public abstract String getName();

	public abstract float getMass();

	public abstract float getRadius();

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public abstract RenderLevel getRenderLevel();

	public RenderLevel getDespawnLevel() {
		return getRenderLevel();
	}

	public final void despawn() {
		destroyed = true;
		for(Runner runner : new ArrayList<>(getStartedRunners())) {
			runner.cancel();
		}
		getWorld().remove(this);
	}

	public final List<Runner> getStartedRunners() {
		return startedRunners;
	}

	public Runner start(Action action) {
		Runner runner = new Runner(this, action);
		runner.start();
		return runner;
	}

	public final void notifyRunner(Runner runner) {
		if(runner.getObject() != this) {
			return;
		}
		boolean hasThisRunner = false;
		for(int i = startedRunners.size() - 1; i >= 0; i--) {
			Runner a = startedRunners.get(i);
			if(a == runner) {
				hasThisRunner = true;
			}
			else if(a.getState() != RunnerState.STARTED) {
				startedRunners.remove(i);
			}
		}
		if(!hasThisRunner) {
			startedRunners.add(runner);
		}
	}

	public final float getAliveTime() {
		return aliveTime;
	}

	public boolean impartsGravity() {
		return false;
	}

	public abstract float getSpecificHeat();

	/**
	 * Get the temperature of the object (degrees Kelvin).
	 *
	 * @return Temperature (Kelvin)
	 */
	public final float getTemperatureKelvin() {
		return temperature;
	}

	/**
	 * Get the temperature in Celsius for convenience.
	 *
	 * @return Temperature (Celsius)
	 */
	public final float getTemperatureCelsius() {
		return getTemperatureKelvin() - 273.15F;
	}

	/**
	 * Set the temperature to a positive number (degrees Kelvin).
	 */
	public void setTemperatureKelvin(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * Add heat to the object with respect to its mass and specific heat.
	 *
	 * @param heat Amount of heat to add
	 */
	public void addHeat(float heat) {
		this.temperature += heat / getMass() / getSpecificHeat();
	}

	//	/**
	//	 * Exposes the object to a temperature over a specified period of time
	//	 */
	//	public void transferTemperature(float other, float duration) {
	//		// TODO implement
	//		throw new RuntimeException("NYI");
	//	}

	public final PVector getPosition() {
		return position.copy();
	}

	public final PVector getPositionReference() {
		return position;
	}

	public final PVector getVelocity() {
		return velocity.copy();
	}

	public final PVector getVelocityReference() {
		return velocity;
	}

	public final void setVelocity(PVector velocity) {
		this.velocity.set(velocity);
	}

	public final void addVelocity(PVector delta) {
		this.velocity.add(delta);
	}

	public final void subVelocity(PVector delta) {
		this.velocity.sub(delta);
	}

	public final void applyVelocity(PVector velocity) {
		this.position.add(velocity.copy().mult(getWorld().getTimeScale()));
	}

	public boolean shouldIgnoreGravity(SpaceObject object) {
		return false;
	}

	public PVector getGravityAcceleration(List<SpaceObject> objects) {
		PVector influence = new PVector();
		for(SpaceObject s : objects) {
			if(shouldIgnoreGravity(s)) {
				continue;
			}
			float distSq = distSq(position, s.getPosition());
			if(distSq == 0) {
				continue; // If the planet being checked is itself (or directly on top), don't move
			}
			float accel = G * s.getMass() / distSq; // Operation order affects precision
			if(Float.isFinite(accel)) {
				influence.add(new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag(accel));
			}
		}
		// Prevent insane acceleration
		return influence/*.limit(MAX_G_FORCE)*/.mult(getWorld().getTimeScale());
	}

	/**
	 * Applies and returns the influence vector of another object on this
	 */
	public PVector applyGravity(List<SpaceObject> objects) {
		PVector acceleration = getGravityAcceleration(objects);
		addVelocity(acceleration);
		return acceleration;
	}

	/**
	 * Update the object's position around a new global origin
	 */
	public void updateOrigin(PVector offset) {
		position.add(offset);
		//		for(int i = 0; i < trail.length; i++) {
		//			if(trail[i] != null) {
		//				trail[i].add(offset);
		//			}
		//		}
	}

	/**
	 * Simulate the object's movement over a certain time interval (used to account for server latency).
	 */
	public void syncMovement(PVector position, PVector velocity, float delay, int interval) {
		//		PVector velocityChange = velocity.copy().sub(this.velocity);

		// Update baseline velocity
		this.velocity.set(velocity);

		// Add velocity towards intended object position
		this.velocity.add(position.copy().sub(this.position).mult(MOTION_SYNC_FACTOR / interval / getWorld().getTimeScale()));

		// Correct for latency
		//		applyVelocity(velocityChange.mult(delay));
	}

	/**
	 * Does this collide with that?
	 */
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return distSq(getPositionReference(), s.getPositionReference()) < sq(getRadius() + s.getRadius());
	}

	/**
	 * Invoked when colliding with SpaceObject `s`
	 */
	public void onCollide(SpaceObject s) {
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public final boolean isDestroyed() {
		return destroyed;
	}

	public final void destroyBecause(SpaceObject reason) {
		if(destroyed) {
			return;
		}
		destroyed = true;
		onDestroyed(reason);
		getWorld().remove(this);

		if(reason instanceof ModularShip && ((ModularShip)reason).hasController()) {
			((ModularShip)reason).getController().emit(PlayerEvent.DESTROY_OBJECT, this);
		}
	}

	/**
	 * Invoked when destroyed by SpaceObject `s`
	 */
	public void onDestroyed(SpaceObject s) {
	}

	public float getOnScreenRadius(float r) {
		return r * 2;
	}

	public void draw(RenderLevel level, float r) {
		RenderLevel thisLevel = getRenderLevel();
		if(thisLevel.isVisibleTo(level)) {
			drawNearby(r);
		}
		else {
			drawDistant(r);
		}
	}

	public void drawNearby(float r) {
		drawDistant(r);
	}

	public void drawDistant(float r) {
	}

	public void drawPreview(float r) {
		v.rotate(v.frameCount * PREVIEW_ROTATE_SPEED);
		//		render(RenderLevel.PARTICLE, r / 3);
		drawNearby(r / 3);
	}

	public void drawMarker() {
		//		v.stroke(v.lerpColor(0, getColor(), sq(1 - getPosition().mag() / WorldGenerator.getRadius(getDespawnLevel()))));

		float outer = MARKER_SIZE * getMarkerScale();
		float inner = outer * .8F;
		v.rect(0, 0, outer, outer);
		v.rect(0, 0, inner, inner);
	}

	public float getMarkerScale() {
		return 1;
	}

	public int getTrailColor() {
		return getColor();
	}

	public int getTrailLength() {
		return DEFAULT_TRAIL_LENGTH;
	}

	public void copyTrail(SpaceObject s) {
		for(int i = 0; i < trail.length; i++) {
			if(i < s.trail.length) {
				trail[i] = s.trail[i];
			}
			else {
				trail[i] = null;
			}
		}
	}

	public void updateTrail() {
		// Update trail vectors
		System.arraycopy(trail, 0, trail, 1, trail.length - 1);
		//		for(int i = trail.length - 1; i > 0; i--) {
		//			trail[i] = trail[i-1];
		//		}
		//				trail[0] = getPosition();
		trail[0] = new float[] {0, 0};
	}

	public void drawTrail(float scale) {
		if(trail.length == 0) {
			return;
		}

		PVector relative = getVelocity().mult(-getWorld().getTimeScale());
		int color = getTrailColor();

		for(int i = 1; i < trail.length; i++) {
			float[] oldPos = trail[i - 1];
			float[] newPos = trail[i];
			if(trail[i] == null) {
				break;
			}

			newPos[0] += relative.x;
			newPos[1] += relative.y;

			// Set the color and render the line segment
			v.stroke(v.lerpColor(color, 0, (float)i / trail.length));
			v.line(oldPos[0] / scale, oldPos[1] / scale, newPos[0] / scale, newPos[1] / scale);
		}
	}

	/**
	 * Perform physics updates for this SpaceObject.
	 */
	public final void update(RenderLevel level) {
		aliveTime += 1 / v.frameRate;
		onUpdate(level);
	}

	public void updateTargets() {
	}

	public void onUpdate(RenderLevel level) {
	}

	public void observe(ObservationLevel level, Player player) {
	}

	//// Convenience methods

	public final PVector relativePosition(SpaceObject other) {
		return other.getPosition().sub(getPosition());
	}

	public final PVector relativeVelocity(SpaceObject other) {
		return other.getVelocity().sub(getVelocity());
	}
}

