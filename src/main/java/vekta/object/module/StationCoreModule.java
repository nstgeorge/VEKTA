package vekta.object.module;

import static vekta.Vekta.v;

public class StationCoreModule extends ShipModule {
	public StationCoreModule() {

	}

	@Override
	public String getName() {
		return "Station Core";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.HULL;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public Module getVariant() {
		return new StationCoreModule();
	}

	@Override
	public void draw(float tileSize) {
		v.rect(0, 0, tileSize, tileSize);
	}
}
