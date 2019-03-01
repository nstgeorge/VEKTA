package vekta.object;

import processing.core.PVector;
import vekta.Counter;
import vekta.Resources;

import static vekta.Vekta.*;

public class PirateShip extends Ship implements Targeter {
	// 'Ere be PirateShip defaults
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F;
	private static final float DEF_TURN = 20;
	private static final float ATTACK_DIST = 1000;
	private static final float PROJECTILE_SPEED = 10;
	private static final float MIN_ATTACK = 10;
	private static final float MAX_ATTACK = 100;
	private static final float ATTACK_SPREAD = 10;

	private SpaceObject target;
	private final Counter attackCt = new Counter();

	public PirateShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
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
		return obj instanceof Ship && !(obj instanceof PirateShip);
	}

	@Override
	public void onUpdate() {
		if(target != null) {
			PVector pos = target.getPosition().copy()
					.add(target.getVelocity().sub(velocity).div(PROJECTILE_SPEED / 2));
			PVector offset = pos.sub(position);

			heading.set(offset.copy().normalize());
			float rangeFactor = ATTACK_DIST * ATTACK_DIST - offset.magSq();
			accelerate(max(-.5F, min(1F, -rangeFactor)));
			if(rangeFactor >= -ATTACK_DIST * ATTACK_DIST / 16 && attackCt.cycle()) {
				fireProjectile();
				attackCt.delay((int)getInstance().random(MIN_ATTACK, MAX_ATTACK));
			}
		}
	}

	@Override
	public void draw() {
		drawShip(SHIP_SHAPE.FIGHTER);
	}

	private void fireProjectile() {
		Resources.playSound("laser");
		PVector vel = heading.copy().setMag(PROJECTILE_SPEED)
				.rotate(getInstance().random(-ATTACK_SPREAD, ATTACK_SPREAD) * PI / 360)
				.add(velocity);
		addObject(new Projectile(this, position.copy(), vel, getColor()));
	}
}  
