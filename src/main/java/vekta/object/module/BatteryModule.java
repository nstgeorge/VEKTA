package vekta.object.module;

public class BatteryModule extends ShipModule {
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
	public Module getVariant() {
		return new BatteryModule(chooseInclusive(1, 20) * 10);
	}

	@Override
	public void onInstall() {
		getShip().addMaxEnergy(getCapacity());
	}

	@Override
	public void onUninstall() {
		getShip().addMaxEnergy(-getCapacity());
	}
}
