package vekta.object;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Sync;

import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.v;

public class HomingProjectile extends Projectile {
	private static final float HOMING_ACCEL = .2F;
	private static final float HOMING_DAMPEN = .98F;

	private @Sync SpaceObject target;
	private final float speed;

	public HomingProjectile(SpaceObject parent, SpaceObject target, float speed, PVector position, PVector velocity, int color) {
		super(parent, position, velocity, color);

		this.target = target;
		this.speed = speed;
	}

	@Override
	public float getRadius() {
		return 3;
	}

	public SpaceObject getTarget() {
		return target;
	}

	public void setTarget(SpaceObject target) {
		this.target = target;
		syncChanges();
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		if(target != null) {
			if(target.isDestroyed()) {
				target = null;
			}
			else {
				float distSq = relativePosition(target).magSq();
				float speedSq = relativeVelocity(target).magSq();
				PVector pos = target.getPosition().add(target.getVelocity().mult(sqrt(speedSq / distSq)));
				setVelocity(getVelocity()
						.add(pos.sub(getPosition()).setMag(getSpeed() * HOMING_ACCEL))
						.mult(HOMING_DAMPEN));
			}
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
		v.vertex(0, -r);
		v.vertex(-r, r);
		v.vertex(r, r);
		v.endShape(CLOSE);
	}
}  
