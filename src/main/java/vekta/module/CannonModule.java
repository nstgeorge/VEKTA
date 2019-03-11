package vekta.module;

import processing.core.PVector;
import vekta.object.ship.ModularShip;
import vekta.object.Projectile;

import static vekta.Vekta.*;

public class CannonModule extends WeaponModule {
	private static final float PROJECTILE_SPEED = 20;

	@Override
	public String getName() {
		return "Plasma Cannon";
	}

	@Override
	public Module getVariant() {
		return new CannonModule();
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		if(ship.consumeEnergy(.5F)) {
			getWorld().playSound("laser", ship.getPosition());
			PVector velocity = ship.getVelocity().add(ship.getHeading().setMag(PROJECTILE_SPEED));
			register(new Projectile(ship, ship.getPosition(), velocity, ship.getColor()));
		}
	}
}
