package vekta.module;

import vekta.KeyBinding;

public abstract class WeaponModule extends ShipModule {

	@Override
	public ModuleType getType() {
		return ModuleType.WEAPON;
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == getFireKey()) {
			fireWeapon();
		}
	}

	public KeyBinding getFireKey() {
		return KeyBinding.SHIP_ATTACK;
	}

	public abstract void fireWeapon();
}
