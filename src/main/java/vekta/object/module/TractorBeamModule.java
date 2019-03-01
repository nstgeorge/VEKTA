package vekta.object.module;

import vekta.object.Ship;

public class TractorBeamModule implements Module {
	private final float force;

	public TractorBeamModule(float force) {
		this.force = force;
	}

	public float getForce() {
		return force;
	}

	@Override
	public String getName() {
		return "Traction Beam";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TractorBeamModule && getForce() > ((TractorBeamModule)other).getForce();
	}

	@Override
	public void update(Ship ship) {
		// TODO: implement
	}
}
