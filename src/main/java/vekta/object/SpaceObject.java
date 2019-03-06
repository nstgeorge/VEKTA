package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;

import java.util.Collection;
import java.util.List;

import static vekta.Vekta.*;

public abstract class SpaceObject {
	private static final float MARKER_SIZE = 40;
	private static final int TRAIL_LENGTH = 100;

	private final PVector[] trail = new PVector[TRAIL_LENGTH];

	private int id;
	private boolean destroyed;

	protected final PVector position;
	protected final PVector velocity;
	private final int color;

	private float temperature;

	public SpaceObject(int color) {
		this(new PVector(), new PVector(), color);
	}

	public SpaceObject(PVector position, PVector velocity, int color) {
		this.position = position;
		this.velocity = velocity;
		this.color = color;
	}

	/**
	 * Gets the unique ID of an object
	 */
	public final int getID() {
		return id;
	}

	/**
	 * Sets the unique ID of an object
	 */
	public final void setID(int id) {
		this.id = id;
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

	public abstract RenderLevel getRenderLevel();

	public abstract float getSpecificHeat();

	/**
	 * Gets the temerature emission of the object
	 */
	public float getTemperature() {
		return temperature;
	}

	public void addTemperature(float temperature) {
		this.temperature += temperature;
	}

	/**
	 * Exposes the object to a temperature over a specified period of time
	 */
	public void equalizeTemperature(float other, float duration) {
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
	 * Does this collide with that?
	 */
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		if(!getRenderLevel().isVisibleTo(level)) {
			return false;
		}
		return distSq(getPosition(), s.getPosition()) < sq(getRadius() + s.getRadius());
	}

	/**
	 * Invoked when colliding with SpaceObject `s`
	 */
	public void onCollide(SpaceObject s) {
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
		removeObject(this);
	}

	/**
	 * Invoked when destroyed by SpaceObject `s`
	 */
	public void onDestroy(SpaceObject s) {
	}

	public void draw(RenderLevel level, float r) {
		RenderLevel thisLevel = getRenderLevel();
		if(thisLevel.isVisibleTo(level)) {
			drawNearby(r);
		}
		else/* if(thisLevel == level.getBelow())*/ {
			drawDistant(r);
		}
	}

	public void drawNearby(float r) {
		drawDistant(r);
	}

	public void drawDistant(float r) {
		float outer = MARKER_SIZE * getMarkerScale();
		float inner = outer * .8F;
		v.rect(0, 0, outer, outer);
		v.rect(0, 0, inner, inner);
	}

	public float getMarkerScale() {
		return 1;
	}

	public void drawTrail(float scale) {
		// Update trail vectors
		if(trail.length - 1 >= 0) {
			System.arraycopy(trail, 0, trail, 1, trail.length - 1);
		}
		trail[0] = getPosition();

		for(int i = 1; i < trail.length; i++) {
			PVector oldPos = trail[i - 1];
			PVector newPos = trail[i];
			if(newPos == null) {
				break;
			}
			// Set the color and draw the line segment
			v.stroke(v.lerpColor(getColor(), v.color(0), (float)i / trail.length));
			v.line((oldPos.x - position.x) / scale, (oldPos.y - position.y) / scale, (newPos.x - position.x) / scale, (newPos.y - position.y) / scale);
		}
	}

	/**
	 * Perform physics updates for this SpaceObject.
	 */
	public final void update() {
		onUpdate();
		applyVelocity(velocity);
	}

	public void onUpdate() {
	}

	public Collection<Targeter> getTargeters() {
		return null;
	}
}  
