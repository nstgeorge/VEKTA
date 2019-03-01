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
	public void accelerate(Ship ship, float amount) {
		ship.accelerate(getSpeed() * amount);
	}
}
