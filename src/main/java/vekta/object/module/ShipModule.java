package vekta.object.module;

import vekta.object.ControllableShip;

public abstract class ShipModule implements Module {
	private ControllableShip ship;
	
	public ControllableShip getShip() {
		return ship;
	}
	
	@Override
	public final void onInstall(ControllableShip ship) {
		this.ship = ship;
		onInstall();
	}

	@Override
	public final void onUninstall(ControllableShip ship) {
		onUninstall();
		this.ship = null;
	}

	public void onInstall() {
	}

	public void onUninstall() {
	}
}
