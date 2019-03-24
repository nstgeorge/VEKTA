package vekta.module;

public class GeneratorModule extends ShipModule {
	private static final float GENERATOR_HEAT = 5e4F;

	private final float rate;

	public GeneratorModule() {
		this(1);
	}

	public GeneratorModule(float rate) {
		this.rate = rate;
	}

	public float getRate() {
		return rate;
	}

	@Override
	public String getName() {
		return "Nuclear Generator v" + getRate();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public int getMass() {
		return (int)((getRate() + 2) * 1000);
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof GeneratorModule && getRate() > ((GeneratorModule)other).getRate();
	}

	@Override
	public Module getVariant() {
		return new GeneratorModule(chooseInclusive(.5F, 2, .5F));
	}

	@Override
	public void onUpdate() {
		float amount = 10 * getRate() * PER_MINUTE;
		if(getShip().getEnergy() < getShip().getMaxEnergy() && !getShip().isOverheated()) {
			getShip().recharge(amount);
			getShip().addHeat(amount * GENERATOR_HEAT);
		}
	}
}
