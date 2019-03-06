package vekta.object;

import processing.core.PVector;
import vekta.RenderDistance;

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

	public SpaceObject getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return "Projectile";
	}

	@Override
	public float getMass() {
		return mass;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public void onUpdate() {
		if(++aliveTime >= DESPAWN_TIME) {
			removeObject(this);
		}
	}

	@Override
	public void draw(RenderDistance dist) {
		if(dist.isNearby()) {
			v.ellipse(0, 0, radius, radius);
		}
	}

	@Override
	public boolean collidesWith(SpaceObject s) {
		return s != getParent() && super.collidesWith(s);
	}

	/**
	 * Override to always destroy projectiles on impact
	 */
	@Override
	public void onCollide(SpaceObject s) {
		destroyBecause(s);
		s.destroyBecause(this); // TODO: switch to imparting damage
		getWorld().playSound("explosion", getPosition());
	}
}  
