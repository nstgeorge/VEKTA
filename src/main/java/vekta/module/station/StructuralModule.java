package vekta.module.station;

import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ShipModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

public class StructuralModule extends ShipModule {
	private final int width, height;

	public StructuralModule(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String getName() {
		return "Structural Beam " + getWidth() + "x" + getHeight();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public Module getVariant() {
		return new StructuralModule(chooseInclusive(1, 3), chooseInclusive(1, 3));
	}

	@Override
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
