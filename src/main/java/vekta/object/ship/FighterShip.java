package vekta.object.ship;

import processing.core.PVector;
import vekta.Counter;
import vekta.RenderLevel;
import vekta.object.HomingProjectile;
import vekta.object.Projectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;

import static vekta.Vekta.*;

public abstract class FighterShip extends Ship implements Targeter {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F;
	private static final float DEF_TURN = 4;
	private static final float DEF_ENGAGE_DIST = 2000;

	private static final float PROJECTILE_SPEED = 20;
	private static final float MIN_ATTACK = 10;
	private static final float MAX_ATTACK = 200;
	private static final float ATTACK_SPREAD = 10;
	private static final float SPEED_DAMPEN = .01F;

	private SpaceObject target;
	private final Counter attackCt = new Counter();

	public FighterShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
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
	public SpaceObject getSpaceObject() {
		return this;
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject obj) {
		this.target = obj;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		return obj instanceof Ship && !obj.isDestroyed();
	}

	public float getEngageDistance() {
		return DEF_ENGAGE_DIST;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(target != null) {
			PVector pos = target.getPosition().add(relativeVelocity(target).div(PROJECTILE_SPEED));
			PVector offset = pos.sub(position);

			float engageSq = sq(getEngageDistance());
			float rangeFactor = engageSq - offset.magSq();

			heading.set(offset.normalize());

			accelerate(max(-.5F, min(1F, -rangeFactor)));
			if(target.getColor() != getColor() && rangeFactor >= 0 && attackCt.cycle()) {
				fireProjectile();
				attackCt.delay(chooseAttackTime());
			}

			addVelocity(relativeVelocity(target).mult(SPEED_DAMPEN));
		}
	}

	@Override
	public void updateTargets() {
		getWorld().updateTargeter(this);
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.FIGHTER);
	}

	@Override
	public boolean isDockable(SpaceObject s) {
		return s instanceof TerrestrialPlanet && ((TerrestrialPlanet)s).getTerrain().isInhabited();
	}

	public float getAttackScale() {
		return 1;
	}

	public int chooseAttackTime() {
		return (int)v.random(MIN_ATTACK, MAX_ATTACK);
	}

	public void fireProjectile() {
		getWorld().playSound("laser", this.position);
		PVector vel = getHeading().setMag(PROJECTILE_SPEED)
				.rotate(v.random(-ATTACK_SPREAD, ATTACK_SPREAD) * PI / 360)
				.add(velocity);

		register(v.chance(.2F)
				? new HomingProjectile(this, getTarget(), getAttackScale(), getPosition(), getVelocity(), getColor())
				: new Projectile(this, getPosition(), vel, getColor()));
	}
}  
