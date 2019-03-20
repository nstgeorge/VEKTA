package vekta.object;

import processing.core.PVector;
import vekta.Player;
import vekta.PlayerEvent;
import vekta.RenderLevel;
import vekta.Syncable;
import vekta.knowledge.KnowledgeLevel;
import vekta.object.ship.ModularShip;

import java.io.Serializable;
import java.util.List;

import static vekta.Vekta.*;

public abstract class SpaceObject extends Syncable<SpaceObject> implements Serializable {
	private static final float PREVIEW_ROTATE_SPEED = 5e-3F;
	private static final float MARKER_SIZE = 40;
	private static final int DEFAULT_TRAIL_LENGTH = 50;
	private static final float MOTION_SYNC_FACTOR = .2F; // How much to over/undercorrect object motion

	protected final PVector[] trail;

	private boolean persistent;
	private boolean destroyed;

	protected final PVector position = new PVector();
	protected final PVector velocity = new PVector();
	private int color;

	private float temperature;

	public SpaceObject(PVector position, PVector velocity, int color) {
		this.position.set(position);
		this.velocity.set(velocity);
		this.color = color;

		this.trail = new PVector[getTrailLength()];
	}

	/**
	 * Gets the name of the object
	 */
	public abstract String getName();

	/**
	 * Gets the mass of the object
	 */
	public abstract float getMass();

	/**
	 * Gets the radius of the object (for collision purposes, not all objects are circular)
	 */
	public abstract float getRadius();

	/**
	 * Gets the color of the object
	 */
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

	public void despawn() {
		destroyed = true;
		getWorld().remove(this);
	}

	public boolean impartsGravity() {
		return false;
	}

	public abstract float getSpecificHeat();

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public void addHeat(float heat) {
		this.temperature += heat / getMass() / getSpecificHeat();
	}

	/**
	 * Exposes the object to a temperature over a specified period of time
	 */
	public void transferTemperature(float other, float duration) {
		// TODO implement
	}

	/**
	 * Gets the position of the object
	 */
	public final PVector getPosition() {
		return position.copy();
	}

	/**
	 * Gets a reference to the object's position
	 */
	public final PVector getPositionReference() {
		return position;
	}

	/**
	 * Gets the velocity of the object
	 */
	public final PVector getVelocity() {
		return velocity.copy();
	}

	/**
	 * Gets a reference to the object's velocity
	 */
	public final PVector getVelocityReference() {
		return velocity;
	}

	/**
	 * Sets the velocity of the object
	 */
	public final void setVelocity(PVector velocity) {
		this.velocity.set(velocity);
	}

	/**
	 * Adds velocity to the object
	 */
	public final void addVelocity(PVector delta) {
		this.velocity.add(delta);
	}

	/**
	 * Subtracts velocity from object
	 *
	 * @param delta
	 */
	public final void subVelocity(PVector delta) {
		this.velocity.sub(delta);
	}

	/**
	 * Applies the given velocity to the object
	 */
	public final void applyVelocity(PVector velocity) {
		this.position.add(velocity.copy().mult(getWorld().getTimeScale()));
	}

	public PVector getGravityAcceleration(List<SpaceObject> objects) {
		PVector influence = new PVector();
		for(SpaceObject s : objects) {
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
		return distSq(getPosition(), s.getPosition()) < sq(getRadius() + s.getRadius());
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
		onDestroy(reason);
		getWorld().remove(this);

		if(reason instanceof ModularShip && ((ModularShip)reason).hasController()) {
			((ModularShip)reason).getController().emit(PlayerEvent.DESTROY_OBJECT, this);
		}
	}

	/**
	 * Invoked when destroyed by SpaceObject `s`
	 */
	public void onDestroy(SpaceObject s) {
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
		draw(RenderLevel.PARTICLE, r / 3);
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

	public void updateTrail() {
		// Update trail vectors
		System.arraycopy(trail, 0, trail, 1, trail.length - 1);
		//		trail[0] = getPosition();
		trail[0] = new PVector();
	}

	public void drawTrail(float scale) {
		int color = getTrailColor();
		PVector relative = getVelocity().mult(-getWorld().getTimeScale());
		for(int i = 1; i < trail.length; i++) {
			PVector oldPos = trail[i - 1];
			PVector newPos = trail[i];
			if(newPos == null) {
				break;
			}
			newPos.add(relative);
			// Set the color and draw the line segment
			v.stroke(v.lerpColor(color, 0, (float)i / trail.length));
			v.line((oldPos.x/* - position.x*/) / scale, (oldPos.y/* - position.y*/) / scale, (newPos.x/* - position.x*/) / scale, (newPos.y/* - position.y*/) / scale);
		}
	}

	/**
	 * Perform physics updates for this SpaceObject.
	 */
	public final void update(RenderLevel level) {
		//		applyVelocity(velocity);
		onUpdate(level);
	}

	public void updateTargets() {
	}

	public void onUpdate(RenderLevel level) {
	}

	public void observe(KnowledgeLevel level, Player player) {
	}

	//// Convenience methods

	public PVector relativePosition(SpaceObject other) {
		return other.getPosition().sub(getPosition());
	}

	public PVector relativeVelocity(SpaceObject other) {
		return other.getVelocity().sub(getVelocity());
	}
}

