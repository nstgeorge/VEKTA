package vekta.module;

import vekta.KeyBinding;

public abstract class WeaponModule extends ShipModule {

	@Override
	public ModuleType getType() {
		return ModuleType.WEAPON;
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_FIRE) {
			fireWeapon();
		}
	}

	public abstract void fireWeapon();
}
