package vekta.module;

import processing.core.PVector;
import vekta.object.CargoCrate;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.min;

public class TractorBeamModule extends ShipModule implements Targeter {
	private static final float BASE_STRENGTH = 1;
	private static final float MAX_FORCE = 1;
	private static final float VELOCITY_DECAY = .05F;

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
	public float getValueScale() {
		return 1 * getForce();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof TractorBeamModule && getForce() > ((TractorBeamModule) other).getForce();
	}

	@Override
	public BaseModule createVariant() {
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
		SpaceObject target = getTarget();
		if (target != null && ship.consumeEnergyOverTime(.2F * PER_SECOND)) {
			float force = getForce() * BASE_STRENGTH;
			PVector dir = target.relativePosition(ship);
			PVector vel = dir.mult(min(MAX_FORCE, force / dir.mag()));
			target.addVelocity(vel.add(target.relativeVelocity(ship).mult(getWorld().getTimeScale() * VELOCITY_DECAY)));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Pick up cargo crates and other small objects with ease.");
	}
}
