package vekta.object.ship;

import processing.core.PVector;
import vekta.Counter;
import vekta.Player;
import vekta.RenderLevel;
import vekta.menu.Menu;
import vekta.object.HomingProjectile;
import vekta.object.Projectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.*;

public class FighterShip extends Ship implements Targeter {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F;
	private static final float DEF_TURN = 4;
	private static final float DEF_ENGAGE_DIST = WorldGenerator.getRadius(RenderLevel.SHIP) * .1F;

	private static final float PROJECTILE_SPEED = 10;
	private static final float MIN_ATTACK = 10;
	private static final float MAX_ATTACK = 200;
	private static final float ATTACK_SPREAD = 10;
	private static final float SPEED_DAMPEN = .98F;

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
	public void setTarget(SpaceObject target) {
		this.target = target;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		return obj instanceof Ship;
	}

	public float getEngageDistance() {
		return DEF_ENGAGE_DIST;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(target != null && target.getColor() != getColor()) {
			PVector pos = target.getPosition().copy()
					.add(target.getVelocity().sub(velocity).div(PROJECTILE_SPEED / 2));
			PVector offset = pos.sub(position);

			heading.set(offset.copy().normalize());
			float engageSq = sq(getEngageDistance());
			float rangeFactor = engageSq - offset.magSq();
			accelerate(max(-.5F, min(1F, -rangeFactor)));
			if(rangeFactor >= 0 && attackCt.cycle()) {
				fireProjectile();
				attackCt.delay((int)v.random(MIN_ATTACK, MAX_ATTACK));
			}
		}
		velocity.mult(SPEED_DAMPEN);
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
	public void setupDockingMenu(Player player, Menu menu) {
		menu.close();
	}

	public void fireProjectile() {
		getWorld().playSound("laser", this.position);
		PVector vel = heading.copy().setMag(PROJECTILE_SPEED)
				.rotate(v.random(-ATTACK_SPREAD, ATTACK_SPREAD) * PI / 360)
				.add(velocity);

		SpaceObject projectile = v.random(1) < .2F
				? new HomingProjectile(this, getTarget(), 1.5F, getPosition(), getVelocity(), getColor())
				: new Projectile(this, getPosition(), vel, getColor());
		addObject(projectile);
	}
}  
