package vekta.object.module;

import processing.core.PVector;
import vekta.object.*;

import static vekta.Vekta.min;

public class TractorBeamModule implements Module, Targeter {
	private static final float BASE_STRENGTH = 100;
	private static final float MAX_FORCE = 1F;
	private static final float VELOCITY_DECAY = .95F;

	private final float force;

	private SpaceObject target;

	public TractorBeamModule(float force) {
		this.force = force;
	}

	public float getForce() {
		return force;
	}

	@Override
	public String getName() {
		return "Tractor Beam v" + getForce();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TractorBeamModule && getForce() > ((TractorBeamModule)other).getForce();
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject target) {
		this.target = target;
	}

	@Override public boolean isValidTarget(SpaceObject obj) {
		return obj instanceof CargoCrate;
	}

	@Override public boolean shouldResetTarget() {
		return true;
	}

	@Override
	public void onUpdate(Ship ship) {
		SpaceObject t = getTarget();
		if(t != null) {
			float force = getForce() / t.getMass() * BASE_STRENGTH;
			PVector dir = ship.getPosition().sub(t.getPosition());
			PVector vel = dir.mult(min(MAX_FORCE, force / dir.magSq()));
			t.setVelocity(vel.add(t.getVelocity().mult(VELOCITY_DECAY)));
		}
	}
}
