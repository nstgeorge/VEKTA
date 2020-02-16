package vekta.object;

import processing.core.PVector;
import vekta.world.RenderLevel;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class FractalProjectile extends Projectile {

	private static final float SPLITS_PER_SECOND = 2;
	private static final float MAX_SPLIT = 3;
	private static final float MAX_SPLIT_ANGLE = .3F;

	private final int charge;

	public FractalProjectile(SpaceObject parent, PVector position, PVector velocity, int color, int charge) {
		super(parent, position, velocity, color);

		this.charge = charge;
	}

	public int getCharge() {
		return charge;
	}

	@Override
	public int getDespawnTime() {
		return 200;
	}

	@Override
	public int getColor() {
		return getTrailColor();
	}

	@Override
	public int getTrailColor() {
		return v.lerpColor(super.getTrailColor(), 0, (float)getAliveTime() / getDespawnTime());
	}

	@Override
	public void onUpdate(RenderLevel level) {
		super.onUpdate(level);

		if(getCharge() > 0 && v.chance(SPLITS_PER_SECOND / v.frameRate)) {
			int splits = (int)v.random(MAX_SPLIT) + 1;
			for(int i = 0; i < splits; i++) {
				float rotation = v.random(-MAX_SPLIT_ANGLE, MAX_SPLIT_ANGLE);
				Projectile projectile = register(new FractalProjectile(
						getParentObject(),
						getPosition(),
						getVelocity().rotate(rotation).mult(v.random(.7F, 1)),
						getTrailColor(),
						getCharge() - 1));
				projectile.copyTrail(this);
			}
			despawn();
		}
	}
}  
