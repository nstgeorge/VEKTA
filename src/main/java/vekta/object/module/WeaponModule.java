package vekta.object.module;

import vekta.object.Ship;

public abstract class WeaponModule implements Module {

	@Override
	public ModuleType getType() {
		return ModuleType.WEAPON;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void onKeyPress(Ship ship, char key) {
		if(key == 'x') {
			fireWeapon(ship);
		}
	}

	public abstract void fireWeapon(Ship ship);
}
