package vekta.module;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

import processing.core.PVector;
import vekta.object.HomingProjectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

public class TorpedoModule extends WeaponModule {
	private final float speed;

	public TorpedoModule() {
		this(1);
	}

	public TorpedoModule(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return "Torpedo Launcher v" + getSpeed();
	}

	@Override
	public BaseModule createVariant() {
		return new TorpedoModule(chooseInclusive(1, 4));
	}

	@Override
	public int getMass() {
		return 1200;
	}

	@Override
	public float getValueScale() {
		return 1.5F * getSpeed();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof TorpedoModule && getSpeed() > ((TorpedoModule) other).getSpeed();
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		BaseModule m = ship.getModule(ModuleType.NAVIGATION);
		if (m instanceof Targeter) {
			SpaceObject target = ((Targeter) m).getTarget();
			if (target != null && ship.consumeEnergyImmediate(1 * getSpeed())) {
				getWorld().playSound("laser", ship.getPosition());
				PVector velocity = ship.getHeading().add(PVector.random2D()).add(ship.getVelocity());
				register(new HomingProjectile(ship, target, getSpeed(), ship.getPosition(), velocity, ship.getColor()));
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Pick a target, fire, and enjoy.");

		super.onInfo(info);
	}
}
