package vekta.module;

public class ActiveTCSModule extends PassiveTCSModule {
	private static final float EFFICIENCY_BOOST = 100;

	private boolean active = false;

	public ActiveTCSModule() {
		super();
	}

	public ActiveTCSModule(float efficiency) {
		super(efficiency);
	}

	@Override
	public String getName() {
		return "Active TCS v" + getEfficiency();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.THERMAL;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof ActiveTCSModule
				? getEfficiency() > ((ActiveTCSModule)other).getEfficiency()
				: other instanceof PassiveTCSModule;
	}

	@Override
	public Module getVariant() {
		return new ActiveTCSModule(chooseInclusive(.5F, 3, .5F));
	}

	@Override
	public void onUninstall() {
		active = false;
	}

	@Override
	public void onUpdate() {
		if(getShip().getTemperature() >= getShip().getCooldownTemperature()) {
			active = true;
		}
		applyCooling(active && getShip().consumeEnergy(1 * getEfficiency() * PER_SECOND) ? EFFICIENCY_BOOST : 1);
		if(getShip().getTemperature() <= getShip().getOptimalTemperature()) {
			active = false;
		}
	}
}
