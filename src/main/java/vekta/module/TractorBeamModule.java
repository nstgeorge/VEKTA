package vekta.module;

import processing.core.PVector;
import vekta.object.CargoCrate;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.min;

public class TractorBeamModule extends ShipModule implements Targeter {
	private static final float BASE_STRENGTH = 100;
	private static final float MAX_FORCE = 1F;
	private static final float VELOCITY_DECAY = .95F;

	private final float force;

	private SpaceObject target;

	public TractorBeamModule() {
		this(1);
	}

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
	public int getMass() {
		return 800;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TractorBeamModule && getForce() > ((TractorBeamModule)other).getForce();
	}

	@Override
	public Module getVariant() {
		return new TractorBeamModule(chooseInclusive(1, 2));
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getShip();
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
		return obj instanceof CargoCrate;
	}

	@Override
	public boolean shouldUpdateTarget() {
		return true;
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();
		SpaceObject t = getTarget();
		if(t != null && ship.consumeEnergyOverTime(.2F * PER_SECOND)) {
			float force = getForce() * BASE_STRENGTH;
			PVector dir = ship.getPosition().sub(t.getPosition());
			PVector vel = dir.mult(min(MAX_FORCE, force / dir.magSq()));
			t.setVelocity(vel.add(t.getVelocity().mult(VELOCITY_DECAY)));
		}
	}
}
