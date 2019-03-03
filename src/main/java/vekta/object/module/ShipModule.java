package vekta.object.module;

import vekta.object.ControllableShip;

import static vekta.Vekta.getInstance;
import static vekta.Vekta.round;

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

	protected final float choose(float min, float max) {
		return getInstance().random(min, max);
	}

	protected final int chooseInclusive(int min, int max) {
		return round(getInstance().random(min, max));
	}
}
