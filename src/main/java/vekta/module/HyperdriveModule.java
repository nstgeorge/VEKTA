package vekta.module;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.*;

public class HyperdriveModule extends ShipModule {
	//	private static final float AUTO_SUSTAIN = 100;
	private static final float MIN_SPEED = 10;
	private static final float MAX_SPEED = 50;

	private final float boost;

	private boolean active;
	private float currentBoost;

	public HyperdriveModule() {
		this(1);
	}

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
		return ModuleType.HYPERDRIVE;
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
		active = false;
		currentBoost = 0;
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();

		float thrust = ship.isLanding() ? -1 : ship.getThrustControl();
		currentBoost = max(0, min(MAX_SPEED, ship.getVelocity().dot(ship.getHeading()))) * getBoost();
		//		sustain = thrust > 0 ? sustain + 1 : 0;

		boolean movingFast = ship.getVelocity().magSq() >= sq(MIN_SPEED * getWorld().getTimeScale());
		//		if(movingFast && sustain >= AUTO_SUSTAIN) {
		//			startHyperdrive();
		//		}
		if((!movingFast && thrust < 0) || !ship.hasEnergy()) {
			endHyperdrive();
		}

		if(isActive() && ship.consumeEnergyOverTime(.05F * currentBoost * PER_SECOND)) {
			ship.setVelocity(ship.getHeading().setMag(ship.getVelocity().mag()));
			ship.accelerate(thrust * currentBoost, ship.getVelocity());
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_HYPERDRIVE) {
			if(!isActive()) {
				startHyperdrive();
			}
			else {
				getShip().setLanding(true);
			}
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

	@Override
	public void onMenu(Menu menu) {
		endHyperdrive();
	}
}
