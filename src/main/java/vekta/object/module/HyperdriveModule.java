package vekta.object.module;

import vekta.Resources;
import vekta.object.Ship;

import static vekta.Vekta.*;

public class HyperdriveModule extends EngineModule {
	private static final float MIN_SPEED = 10;

	private final float boost;

	private float currentBoost;

	public HyperdriveModule(float boost) {
		super(1);

		this.boost = boost;
	}

	public float getBoost() {
		return boost;
	}

	@Override 
	public float getSpeed() {
		return super.getSpeed() + currentBoost;
	}

	@Override
	public String getName() {
		return "Hyperdrive v" + getBoost();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ENGINE;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof HyperdriveModule && getSpeed() > ((HyperdriveModule)other).getSpeed();
	}

	@Override 
	public void onUninstall(Ship ship) {
		currentBoost = 0;
	}

	@Override
	public void onUpdate(Ship ship) {
		boolean wasBoosting = currentBoost > 0;
		currentBoost = max(0, ship.getVelocity().mag() * getBoost() - MIN_SPEED);
		if(!wasBoosting && currentBoost > 0) {
			Resources.playSound("hyperdriveHit");
			Resources.loopSound("hyperdriveLoop");
		}
		if(wasBoosting && currentBoost == 0) {
			Resources.stopSound("hyperdriveLoop");
			Resources.playSound("hyperdriveEnd");
		}

		super.onUpdate(ship);
	}
}
