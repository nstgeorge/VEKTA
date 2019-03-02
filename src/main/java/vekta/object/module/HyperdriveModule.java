package vekta.object.module;

import vekta.Resources;
import vekta.object.Ship;

import static vekta.Vekta.*;

public class HyperdriveModule implements Module {
	private static final float MIN_SPEED = 15;
	private static final float MAX_SPEED = 100;

	private final float boost;

	private float currentBoost;

	public HyperdriveModule(float boost) {
		this.boost = boost;
	}

	public float getBoost() {
		return boost;
	}

	@Override
	public String getName() {
		return "Hyperdrive v" + getBoost();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof HyperdriveModule && getBoost() > ((HyperdriveModule)other).getBoost();
	}

	@Override 
	public void onUninstall(Ship ship) {
		currentBoost = 0;
	}

	@Override
	public void onUpdate(Ship ship) {
		boolean wasBoosting = currentBoost > 0;
		currentBoost = ship.isLanding() ? 0 : min(max(0, ship.getVelocity().mag() * getBoost() - MIN_SPEED), MAX_SPEED);
		if(!wasBoosting && currentBoost > 0) {
			Resources.playSound("hyperdriveHit");
			Resources.loopSound("hyperdriveLoop");
		}
		if(wasBoosting && currentBoost == 0) {
			Resources.stopSound("hyperdriveLoop");
			Resources.playSound("hyperdriveEnd");
		}
		ship.accelerate(ship.getThrustControl() * currentBoost, ship.getVelocity());
	}
}
