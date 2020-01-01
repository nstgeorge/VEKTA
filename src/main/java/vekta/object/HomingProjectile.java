package vekta.object;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.sync.Sync;
import vekta.spawner.WorldGenerator;

import static processing.core.PApplet.*;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.v;

public class HomingProjectile extends Projectile {
	private static final float HOMING_ACCEL = .1F;
	private static final float HOMING_DAMPEN = .02F;
	private static final float MAX_SPEEDUP_DIST = WorldGenerator.getRadius(RenderLevel.PARTICLE);

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
				PVector relVel = relativeVelocity(target);
				float distSq = relativePosition(target).magSq();
				float speedSq = relVel.magSq();
				PVector relPos = relativePosition(target).add(target.getVelocity().mult(sqrt(speedSq / distSq)));
				PVector tangent = relPos.copy().rotate(HALF_PI);
				float offsetDistSq = min(sq(MAX_SPEEDUP_DIST), relPos.magSq());
				addVelocity(relPos.setMag(getSpeed() * HOMING_ACCEL)
						.add(tangent.mult(tangent.dot(relVel) / offsetDistSq * getSpeed() * HOMING_DAMPEN)));

				if(target instanceof HomingResponder) {
					((HomingResponder)target).respondIncoming(this);
				}
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
