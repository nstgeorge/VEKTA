package vekta.object.module;

import vekta.object.PlayerShip;
import vekta.object.Ship;

public class BatteryModule implements Module {
	private final float capacity;

	public BatteryModule(float capacity) {
		this.capacity = capacity;
	}

	public float getCapacity() {
		return capacity;
	}

	@Override
	public String getName() {
		return "Battery v" + getCapacity();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.BATTERY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof BatteryModule && getCapacity() > ((BatteryModule)other).getCapacity();
	}

	@Override
	public void onInstall(Ship ship) {
		((PlayerShip)ship).addMaxEnergy(getCapacity());
	}

	@Override
	public void onUninstall(Ship ship) {
		((PlayerShip)ship).addMaxEnergy(-getCapacity());
	}
}
