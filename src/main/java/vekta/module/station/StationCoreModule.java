package vekta.module.station;

import vekta.RenderLevel;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ShipModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

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
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
	}

	@Override
	public void draw(RenderLevel dist, float tileSize) {
		v.rect(0, 0, tileSize, tileSize);
	}
}
