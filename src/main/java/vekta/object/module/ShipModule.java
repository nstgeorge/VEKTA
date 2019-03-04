package vekta.object.module;

import vekta.object.ModularShip;
import vekta.object.SpaceStation;

import static vekta.Vekta.round;
import static vekta.Vekta.v;

public abstract class ShipModule implements ComponentModule {
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
		return v.random(min, max);
	}

	protected final int chooseInclusive(int min, int max) {
		return round(v.random(min, max));
	}

	protected final float chooseInclusive(float min, float max, float interval) {
		return round(v.random(min, max) / interval) * interval;
	}

	public int getWidth() {
		return 1;
	}

	public int getHeight() {
		return 1;
	}

	public boolean hasAttachmentPoint(SpaceStation.Direction dir) {
		return true;
	}

	public void draw(float tileSize) {
		v.rect(0, 0, tileSize / 2, tileSize);
		v.rect(0, 0, tileSize, tileSize / 2);
	}
}
