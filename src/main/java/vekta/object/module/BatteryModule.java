package vekta.object.module;

import vekta.menu.Menu;
import vekta.menu.option.RechargeOption;
import vekta.terrain.LandingSite;

import static java.lang.Math.round;

public class BatteryModule extends ShipModule {
	private final int capacity;

	private float charge;

	public BatteryModule(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public float getCharge() {
		return getShip() != null ? getShip().getEnergy() : charge;
	}

	public float getRatio() {
		return getCharge() / getCapacity();
	}

	@Override
	public String getName() {
		return "Battery v" + getCapacity() + " (" + round(getRatio() * 100) + "%)";
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
		BatteryModule battery = new BatteryModule(chooseInclusive(1, 50) * 10);
		battery.charge = choose(0, battery.getCapacity());
		return battery;
	}

	@Override
	public void onInstall() {
		getShip().setEnergy(getCharge());
		getShip().setMaxEnergy(getCapacity());
	}

	@Override
	public void onUninstall() {
		charge = getShip().getEnergy();
		getShip().setMaxEnergy(0);
	}

	@Override
	public void onLandingMenu(LandingSite site, Menu menu) {
		if(site.getTerrain().hasFeature("Inhabited") && getCharge() <= getCapacity() * .9F) {
			menu.add(new RechargeOption(getShip()));
		}
	}
}
