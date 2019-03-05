package vekta.module;

import vekta.Resources;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.max;
import static vekta.Vekta.min;

public class HyperdriveModule extends ShipModule {
	private static final float MIN_SPEED = 25;
	private static final float MAX_SPEED = 100;

	private final float boost;

	private boolean active;
	private float currentBoost;

	public HyperdriveModule(float boost) {
		this.boost = boost;
	}

	public float getBoost() {
		return boost;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public String getName() {
		return "Hyperdrive v" + getBoost();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof HyperdriveModule && getBoost() > ((HyperdriveModule)other).getBoost();
	}

	@Override
	public Module getVariant() {
		return new HyperdriveModule(chooseInclusive(.1F, 1, .1F));
	}

	@Override
	public void onUninstall() {
		currentBoost = 0;
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();

		float thrust = ship.isLanding() ? -1 : ship.getThrustControl();
		currentBoost = max(0, min(MAX_SPEED, ship.getVelocity().mag() * getBoost()));

		boolean movingFast = ship.getVelocity().magSq() >= MIN_SPEED * MIN_SPEED;
		if(movingFast && thrust > 0) {
			startHyperdrive();
		}
		if((!movingFast && thrust < 0) || !ship.hasEnergy()) {
			endHyperdrive();
		}

		if(isActive() && ship.consumeEnergy(.1F * currentBoost * PER_SECOND)) {
			ship.setVelocity(ship.getHeading().setMag(ship.getVelocity().mag()));
			ship.accelerate(thrust * currentBoost, ship.getVelocity());
		}
	}

	@Override
	public void onKeyPress(char key) {
		if(key == '`') {
			startHyperdrive();
		}
	}

	public void startHyperdrive() {
		if(!isActive()) {
			active = true;
			Resources.playSound("hyperdriveHit");
			Resources.loopSound("hyperdriveLoop");
		}
	}

	public void endHyperdrive() {
		if(isActive()) {
			active = false;
			Resources.stopSound("hyperdriveLoop");
			Resources.playSound("hyperdriveEnd");
		}
	}
}
