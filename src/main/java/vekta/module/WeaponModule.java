package vekta.module;

import vekta.ControlKey;

public abstract class WeaponModule extends ShipModule {

	@Override
	public ModuleType getType() {
		return ModuleType.WEAPON;
	}

	@Override
	public void onKeyPress(ControlKey key) {
		if(key == ControlKey.SHIP_FIRE) {
			fireWeapon();
		}
	}

	public abstract void fireWeapon();
}
