package vekta.object;

import org.reflections.ReflectionUtils;
import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Syncable;
import vekta.spawner.WorldGenerator;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static vekta.Vekta.*;

public abstract class SpaceObject implements Serializable, Syncable<SpaceObject> {
	private static final float MARKER_SIZE = 40;
	private static final int DEFAULT_TRAIL_LENGTH = 100;

	protected final PVector[] trail;

	private int id;
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

	public void setColor(int color) {
		this.color = color;
	}

	public abstract RenderLevel getRenderLevel();

	public abstract float getSpecificHeat();

	public boolean impartsGravity() {
		return false;
	}

	/**
	 * Gets the temerature emission of the object
	 */
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
		getWorld().remove(this);
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
		//		drawMarker();
	}

	public void drawMarker() {
		// Temp
		v.stroke(v.lerpColor(0, getColor(), 1 - getPosition().mag() / WorldGenerator.getRadius(getRenderLevel())));

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
		onUpdate(level);
		applyVelocity(velocity);
	}

	public void updateTargets() {
	}

	public void onUpdate(RenderLevel level) {
	}

	//// Synchronization methods

	@Override
	public String getSyncKey() {
		return String.valueOf(getID());
	}

	@Override
	public SpaceObject getSyncData() {
		return this;
	}

	// Temp
	private static Field MODIFIER_FIELD;

	static {
		try {
			MODIFIER_FIELD = Field.class.getDeclaredField("modifiers");
			MODIFIER_FIELD.setAccessible(true);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onSync(SpaceObject data) {
		try {
			// TODO: refactor
			// Brute-force update for now
			for(Field field : ReflectionUtils.getAllFields(getClass())) {
				// Beat the devil out of the field
				field.setAccessible(true);
				MODIFIER_FIELD.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				// Recursively replace Syncable objects
				Object object = field.get(data);
				if(object instanceof Syncable) {
					field.set(this, register((Syncable)object));
				}
				else if(object instanceof Collection) {
					Collection collection = (Collection)object;
					List buffer = new ArrayList(collection);
					collection.clear();
					for(Object elem : buffer) {
						collection.add(elem instanceof Syncable ? register((Syncable)elem) : elem);
					}
				}
				else if(object instanceof Map) {
					Map map = (Map)object;
					Map buffer = new HashMap<>(map);
					map.clear();
					for(Object key : map.keySet()) {
						Object value = buffer.get(key);
						map.put(key instanceof Syncable ? register((Syncable)key) : key,
								value instanceof Syncable ? register((Syncable)value) : value);
					}
				}
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	//// Convenience methods

	public PVector relativePosition(SpaceObject other) {
		return other.getPosition().sub(getPosition());
	}

	public PVector relativeVelocity(SpaceObject other) {
		return other.getVelocity().sub(getVelocity());
	}
}  
