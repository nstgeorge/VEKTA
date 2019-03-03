package vekta.object.module;

import processing.core.PVector;
import vekta.Resources;
import vekta.object.ControllableShip;
import vekta.object.Projectile;

import static vekta.Vekta.addObject;

public class CannonModule extends WeaponModule {
	private static final float PROJECTILE_SPEED = 7;

	@Override
	public String getName() {
		return "Plasma Cannon";
	}

	@Override
	public Module getVariant() {
		return new CannonModule();
	}

	@Override
	public void fireWeapon() {
		ControllableShip ship = getShip();
		if(ship.consumeEnergy(.5F)) {
			Resources.playSound("laser");
			PVector velocity = ship.getHeading().setMag(PROJECTILE_SPEED).add(ship.getVelocity());
			addObject(new Projectile(ship, ship.getPosition(), velocity, ship.getColor()));
		}
	}
}
