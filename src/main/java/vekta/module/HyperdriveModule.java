package vekta.module;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.object.Shockwave;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.*;

public class HyperdriveModule extends ShipModule {
	private static final float LOW_TIME_SCALE_SPEEDUP = 50;
	private static final float MIN_BOOST = 10;
	private static final float MAX_BOOST = 100;
	private static final float EFFECT_MODIFIER = 3; // Velocity effect of hyperdrive

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
		endHyperdrive();
	}

	@Override
	public void onUpdate() {
		ModularShip ship = getShip();

		float timeScale = getWorld().getTimeScale();
		float thrust = ship.isLanding() ? -1 : ship.getThrustControl();
		currentBoost = max(0, min(MAX_BOOST, ship.getVelocity().dot(ship.getHeading()))) * getBoost();

		boolean movingFast = ship.getVelocity().magSq() >= sq(MIN_BOOST * timeScale);
		if((!movingFast && thrust < 0) || !ship.hasEnergy()) {
			endHyperdrive();
		}

		if(isActive() && ship.consumeEnergyOverTime(.05F * currentBoost * PER_SECOND)) {
			float effectiveThrust = thrust * max(timeScale, LOW_TIME_SCALE_SPEEDUP) / timeScale;
			ship.setVelocity(ship.getHeading().setMag(min(currentBoost * timeScale, ship.getVelocity().mag())));
			ship.accelerate(effectiveThrust * currentBoost, ship.getVelocity());

			// Create shockwave effect
			Shockwave wave = register(new Shockwave(
					getShip(),
					timeScale * 1e-4F * currentBoost,
					(int)v.random(20, 30),
					getShip().getColor()));
			wave.setRadius(1);
			wave.addVelocity(ship.getHeading().mult(-500 * timeScale / currentBoost * (EFFECT_MODIFIER + thrust)));
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
			currentBoost = 0;
			Resources.stopSound("hyperdriveLoop");
			Resources.playSound("hyperdriveEnd");
		}
	}

	@Override
	public void onMenu(Menu menu) {
		endHyperdrive();
	}
}
