package vekta.object;

import processing.core.PVector;
import vekta.Counter;
import vekta.Resources;

import java.util.Collection;
import java.util.Collections;

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
	private static final float SPEED_DAMPEN = .98F;

	private SpaceObject target;
	private final Counter attackCt = new Counter();

	public PirateShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, DEF_MASS, DEF_RADIUS, heading, position, velocity, color, DEF_SPEED, DEF_TURN);
	}

	@Override
	public Collection<Targeter> getTargeters() {
		return Collections.singleton(this);
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
		// return obj instanceof Ship && !(obj instanceof PirateShip);

		// TEMP: prevent random off-screen shooting noises until we have a clean way to ignore distant sounds
		return obj instanceof PlayerShip;
	}

	@Override public boolean isLanding() {
		return false;
	}

	@Override
	public float getThrustControl() {
		return 0; // Not using standard ship controls
	}

	@Override
	public float getTurnControl() {
		return 0;// Not using standard ship controls
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
		velocity.mult(SPEED_DAMPEN);
	}

	@Override
	public void draw() {
		drawShip(ShipModelType.FIGHTER);
	}

	private void fireProjectile() {
		getWorld().playSoundAt("laser", this.position);
		PVector vel = heading.copy().setMag(PROJECTILE_SPEED)
				.rotate(getInstance().random(-ATTACK_SPREAD, ATTACK_SPREAD) * PI / 360)
				.add(velocity);
		addObject(new Projectile(this, position.copy(), vel, getColor()));
	}
}  
