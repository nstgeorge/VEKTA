package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;

import static vekta.Vekta.*;

public class Projectile extends SpaceObject {
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

	public SpaceObject getParent() {
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
		return s.getColor() != getParent().getColor() && super.collidesWith(getRenderLevel(), s); // Always collide regardless of render distance
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
