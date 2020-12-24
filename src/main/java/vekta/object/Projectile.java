package vekta.object;

import processing.core.PVector;
import vekta.object.ship.Damageable;
import vekta.object.ship.Damager;
import vekta.world.RenderLevel;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class Projectile extends SpaceObject implements Damager {
	private final SpaceObject parent;

	public Projectile(SpaceObject parent, PVector position, PVector velocity, int color) {
		super(position, velocity, color);

		this.parent = parent;

		setTemperatureKelvin(1000);
	}

	public float getDespawnTime() {
		return 10;
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
		return 100;
	}

	@Override
	public float getRadius() {
		return 2;
	}

	@Override
	public int getColor() {
		return v.color(200);
	}

	@Override
	public int getTrailColor() {
		return super.getColor();
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(getAliveTime() >= getDespawnTime()) {
			despawn();
		}
	}

	@Override
	public void drawNearby(float r) {
		v.point(0, 0);
	}

	@Override
	public float getMarkerScale() {
		return .2F;
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		// Always collide regardless of render distance
		return super.collidesWith(getRenderLevel(), s) && s != getParentObject();
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof Damageable && ((Damageable)s).damage(getDamage(), this)) {
			getWorld().playSound("explosion", getPosition());
			destroyBecause(s);
		}
	}
}  
