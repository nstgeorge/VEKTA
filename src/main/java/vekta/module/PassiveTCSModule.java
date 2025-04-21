package vekta.module;

import vekta.util.InfoGroup;

public class PassiveTCSModule extends ShipModule {
	private static final float EFFICIENCY_SCALE = 1e3F;

	private final float efficiency;

	public PassiveTCSModule() {
		this(1);
	}

	public PassiveTCSModule(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public String getName() {
		return "Passive TCS v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.THERMAL;
	}

	@Override
	public int getMass() {
		return (int) ((getEfficiency() + 5) * 100);
	}

	@Override
	public float getValueScale() {
		return 3 * getEfficiency();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof PassiveTCSModule && getEfficiency() > ((PassiveTCSModule) other).getEfficiency();
	}

	@Override
	public BaseModule createVariant() {
		return new PassiveTCSModule(chooseInclusive(.5F, 10, .5F));
	}

	@Override
	public void onUpdate() {
		applyCooling(1);
	}

	public void applyCooling(float scale) {
		float optimal = getShip().getOptimalTemperature();
		if (getShip().getTemperatureKelvin() > optimal) {
			getShip().addHeat(-getEfficiency() * scale * EFFICIENCY_SCALE * PER_SECOND);

			// Round to target temperature
			if (getShip().getTemperatureKelvin() < optimal) {
				getShip().setTemperatureKelvin(optimal);
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription(
				"The Passive Thermal Control System (TCS) slowly radiates excess heat away from the spacecraft.");
	}
}
