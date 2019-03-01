package vekta.object.module;

import vekta.object.Ship;

public class RCSModule implements Module {
	private final float turnSpeed;

	public RCSModule(float turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	@Override
	public String getName() {
		return "RCS v" + getTurnSpeed();
	}

	@Override 
	public ModuleType getType() {
		return ModuleType.RCS;
	}

	@Override 
	public boolean isBetter(Module other) {
		return other instanceof RCSModule && getTurnSpeed() > ((RCSModule)other).getTurnSpeed();
	}

	@Override
	public void turn(Ship ship, float amount) {
		ship.turn(getTurnSpeed() * amount);
	}
}
