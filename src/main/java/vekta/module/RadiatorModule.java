package vekta.module;

public class RadiatorModule extends ShipModule {
	private static final float EFFICIENCY_SCALE = 1e3F;

	private final float efficiency;

	public RadiatorModule() {
		this(1);
	}

	public RadiatorModule(float efficiency) {
		this.efficiency = efficiency;
	}

	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public String getName() {
		return "Thermal Radiator v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.RADIATOR;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof RadiatorModule && getEfficiency() > ((RadiatorModule)other).getEfficiency();
	}

	@Override
	public Module getVariant() {
		return new RadiatorModule(chooseInclusive(.5F, 10, .5F));
	}

	@Override
	public void onUpdate() {
		float optimal = getShip().getOptimalTemperature();
		if(getShip().getTemperature() > optimal) {
			getShip().addHeat(-getEfficiency() * EFFICIENCY_SCALE * PER_SECOND);

			// Round to target temperature
			if(getShip().getTemperature() < optimal) {
				getShip().setTemperature(optimal);
			}
		}
	}
}
