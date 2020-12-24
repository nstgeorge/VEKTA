package vekta.object;

import processing.core.PVector;
import vekta.object.ship.Damageable;
import vekta.object.ship.Damager;
import vekta.world.RenderLevel;

import static processing.core.PApplet.sq;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class Countermeasure extends SpaceObject implements Damageable {
	private static final float JITTER_AMOUNT = 1;
	private static final float LOCK_CHANCE = .5F;
	private static final float DESPAWN_TIME = 1000;

	private int aliveTime;

	public Countermeasure(SpaceObject parent, PVector position, PVector velocity) {
		super(position, velocity, v.color(255, 200, 100));

		//		setTemperature(100);

		for(HomingProjectile projectile : getWorld().findObjects(HomingProjectile.class)) {
			if(projectile.getTarget() == parent && v.chance(LOCK_CHANCE)) {
				projectile.setTarget(this);
				break;
			}
		}
	}

	@Override
	public String getName() {
		return "Countermeasure";
	}

	@Override
	public float getMass() {
		return 10;
	}

	@Override
	public float getRadius() {
		return 3;
	}

	@Override
	public int getColor() {
		return v.lerpColor(0, super.getColor(), sq(1 - aliveTime / DESPAWN_TIME));
	}

	@Override
	public int getTrailLength() {
		return super.getTrailLength() / 2;
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
		addVelocity(PVector.random2D().mult(v.random(JITTER_AMOUNT)));

		if(++aliveTime >= DESPAWN_TIME) {
			despawn();
		}
	}

	@Override
	public void drawNearby(float r) {
		v.ellipse(0, 0, r, r);
	}

	@Override
	public float getMarkerScale() {
		return .1F;
	}

	@Override
	public boolean damage(float amount, Damager damager) {
		destroyBecause(damager.getParentObject());
		return true;
	}
}  
