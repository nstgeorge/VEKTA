package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.ship.Damageable;
import vekta.object.ship.Damager;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class Projectile extends SpaceObject implements Damager {
	// Default settings
	private static final float DESPAWN_TIME = 1000;
	private static final float DEF_MASS = 1000;
	private static final int DEF_RADIUS = 2;

	private final SpaceObject parent;

	private int aliveTime = 0;

	public Projectile(SpaceObject parent, PVector position, PVector velocity, int color) {
		super(position, velocity, color);

		this.parent = parent;
	}

	public float getDamage() {
		return 1;
	}

	@Override
	public SpaceObject getAttackObject() {
		return this;
	}

	@Override
	public SpaceObject getParentObject() {
		return parent;
	}

	@Override
	public String getName() {
		return "Projectile";
	}

	@Override
	public float getMass() {
		return DEF_MASS;
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(++aliveTime >= DESPAWN_TIME) {
			getWorld().remove(this);
		}
	}

	@Override
	public void drawNearby(float r) {
		v.ellipse(0, 0, r, r);
	}

	@Override
	public float getMarkerScale() {
		return .2F;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		if(s instanceof Damageable) {
			return ((Damageable)s).isDamageableFrom(this);
		}
		return super.collidesWith(getRenderLevel(), s); // Always collide regardless of render distance
	}

	@Override
	public void onCollide(SpaceObject s) {
		destroyBecause(s);
		if(s instanceof Damageable) {
			((Damageable)s).damage(getDamage(), this);
		}
		getWorld().playSound("explosion", getPosition());
	}
}  
