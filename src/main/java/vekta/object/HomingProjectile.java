package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;

import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.v;

public class HomingProjectile extends Projectile {
	private static final float HOMING_ACCEL = .1F;
	private static final float HOMING_DAMPEN = .98F;

	private static final float RADIUS = 3;
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
	public void onUpdate(RenderLevel level) {
		if(!target.isDestroyed()) {
			float speedSq = target.getVelocity().sub(getVelocity()).magSq();
			float distSq = target.getPosition().sub(getPosition()).magSq();
			PVector pos = target.getPosition().add(target.getVelocity().mult(sqrt(distSq / speedSq)));
			setVelocity(getVelocity().add(pos.sub(getPosition())
					.setMag(getSpeed() * HOMING_ACCEL)).mult(HOMING_DAMPEN));
		}
		super.onUpdate(level);
	}

	@Override
	public void drawNearby(float r) {
		float theta = velocity.heading() + HALF_PI;
		v.fill(0);
		v.stroke(getColor());
		v.rotate(theta);
		v.beginShape();
		v.vertex(0, -RADIUS);
		v.vertex(-RADIUS, RADIUS);
		v.vertex(RADIUS, RADIUS);
		v.endShape(CLOSE);
	}
}  
