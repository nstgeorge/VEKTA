package vekta.module;

public class GeneratorModule extends ShipModule {
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
		return "Energy Generator v" + getRate();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof GeneratorModule && getRate() > ((GeneratorModule)other).getRate();
	}

	@Override
	public Module getVariant() {
		return new GeneratorModule(chooseInclusive(.5F, 5, .5F));
	}

	@Override
	public void onUpdate() {
		getShip().addEnergy(10 * getRate() * PER_MINUTE);
	}
}
