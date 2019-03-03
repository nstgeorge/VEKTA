package vekta.object.module;

public abstract class WeaponModule extends ShipModule {

	@Override
	public ModuleType getType() {
		return ModuleType.WEAPON;
	}

	@Override
	public void onKeyPress(char key) {
		if(key == 'x') {
			fireWeapon();
		}
	}

	public abstract void fireWeapon();
}
