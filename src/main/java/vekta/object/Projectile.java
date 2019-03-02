package vekta.object;

import processing.core.PVector;

import static vekta.Vekta.*;

public class Projectile extends SpaceObject {
	// Default settings
	private static final float DESPAWN_TIME = 1000;
	private static final float DEF_MASS = 1000;
	private static final int DEF_RADIUS = 2;

	private SpaceObject parent;
	private float mass;
	private float radius;
	
	private int aliveTime = 0;

	public Projectile(SpaceObject parent, PVector position, PVector velocity, int color) {
		super(position, velocity, color);
		this.parent = parent;
		this.mass = DEF_MASS;
		this.radius = DEF_RADIUS;
	}

	@Override
	public void onUpdate() {
		if(++aliveTime >= DESPAWN_TIME) {
			removeObject(this);
		}
	}

	@Override
	public void draw() {
		v.stroke(getColor());
		v.fill(0);
		v.ellipseMode(RADIUS);
		v.ellipse(position.x, position.y, radius, radius);
	}

	public SpaceObject getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return "Projectile";
	}

	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public float getMass() {
		return mass;
	}

	@Override
	public boolean collidesWith(SpaceObject s) {
		return s != getParent() && super.collidesWith(s);
	}

	@Override
	public boolean shouldDestroy(SpaceObject other) {
		return true;
	}

	/**
	 * Override to always destroy projectiles on impact
	 */
	@Override
	public void onCollide(SpaceObject s) {
		super.onCollide(s);
		removeObject(this);
		getWorld().playSoundAt("explosion", getPosition());
	}
}  
