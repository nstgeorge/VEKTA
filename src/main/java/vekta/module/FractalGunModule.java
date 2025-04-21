package vekta.module;

import processing.core.PVector;
import vekta.object.FractalProjectile;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import static vekta.Vekta.*;

public class FractalGunModule extends WeaponModule {
	private static final float PROJECTILE_SPEED = 20;

	private final float speed;

	public FractalGunModule() {
		this(1);
	}

	public FractalGunModule(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return "Fractal Gun v" + getSpeed();
	}

	@Override
	public BaseModule createVariant() {
		return new FractalGunModule(chooseInclusive(1, 3));
	}

	@Override
	public int getMass() {
		return 1200;
	}

	@Override
	public float getValueScale() {
		return 2F * getSpeed();
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof FractalGunModule && getSpeed() > ((FractalGunModule) other).getSpeed();
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		if (ship.consumeEnergyImmediate(4)) {
			getWorld().playSound("laser", ship.getPosition());
			PVector velocity = ship.getVelocity().add(ship.getHeading().setMag(PROJECTILE_SPEED)).mult(v.random(.9F, 1.1F));
			register(new FractalProjectile(ship, ship.getPosition(), velocity, ship.getColor(), 3));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Terrify and (occasionally) pulverize your foes.");

		super.onInfo(info);
	}
}
