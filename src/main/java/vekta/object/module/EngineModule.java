package vekta.object.module;

import vekta.object.Ship;

public class EngineModule implements Module {
	private final float speed;

	public EngineModule(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return "Engine v" + getSpeed();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ENGINE;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof EngineModule && getSpeed() > ((EngineModule)other).getSpeed();
	}

	@Override
	public void onUpdate(Ship ship) {
		float thrust = ship.getThrustControl();
		if(ship.consumeEnergy(20 * thrust * PER_MINUTE)) {
			ship.accelerate(thrust * getSpeed());
		}
	}
}
