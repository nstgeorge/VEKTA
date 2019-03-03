package vekta.object;

import processing.core.PVector;

import static processing.core.PApplet.sqrt;

public class HomingProjectile extends Projectile {
	private static final float HOMING_ACCEL = .1F;
	private static final float HOMING_DAMPEN = .98F;

	private final SpaceObject target;
	private final float speed;

	public HomingProjectile(SpaceObject parent, SpaceObject target, float speed, PVector position, PVector velocity, int color) {
		super(parent, position, velocity, color);

		this.target = target;
		this.speed = speed;
	}

	public SpaceObject getTarget() {
		return target;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public void onUpdate() {
		if(!target.isDestroyed()) {
			float speedSq = target.getVelocity().sub(getVelocity()).magSq();
			float distSq = target.getPosition().sub(getPosition()).magSq();
			PVector pos = target.getPosition().add(target.getVelocity().mult(sqrt(distSq / speedSq)));
			setVelocity(getVelocity().add(pos.sub(getPosition())
					.setMag(getSpeed() * HOMING_ACCEL)).mult(HOMING_DAMPEN));
		}
		super.onUpdate();
	}
}  
