package vekta.object.module;

import vekta.object.ModularShip;

import static vekta.Vekta.getInstance;
import static vekta.Vekta.round;

public abstract class ShipModule implements Module {
	private ModularShip ship;

	public final ModularShip getShip() {
		return ship;
	}

	@Override
	public final void onInstall(ModularShip ship) {
		this.ship = ship;
		onInstall();
	}

	@Override
	public final void onUninstall(ModularShip ship) {
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

	protected final float chooseInclusive(float min, float max, float interval) {
		return round(getInstance().random(min, max) * interval) / interval;
	}
}
