package vekta.module;

import processing.core.PVector;
import vekta.InfoGroup;
import vekta.object.Projectile;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class CannonModule extends WeaponModule {
	private static final float PROJECTILE_SPEED = 20;

	@Override
	public String getName() {
		return "Plasma Cannon";
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public Module createVariant() {
		return new CannonModule();
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		if(ship.consumeEnergyImmediate(.25F)) {
			getWorld().playSound("laser", ship.getPosition());
			PVector velocity = ship.getVelocity().add(ship.getHeading().setMag(PROJECTILE_SPEED));
			register(new Projectile(ship, ship.getPosition(), velocity, ship.getColor()));
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Fling chunks of plasma at your enemies.");

		super.onInfo(info);
	}
}
