package vekta.object.module;

import processing.core.PVector;
import vekta.Resources;
import vekta.object.Projectile;
import vekta.object.Ship;

import static vekta.Vekta.addObject;

public class CannonModule extends WeaponModule {
	private static final float PROJECTILE_SPEED = 7;
	
	@Override
	public String getName() {
		return "Plasma Cannon";
	}

	@Override
	public void fireWeapon(Ship ship) {
		if(ship.consumeEnergy(.5F)) {
			Resources.playSound("laser");
			PVector velocity = ship.getHeading().setMag(PROJECTILE_SPEED).add(ship.getVelocity());
			addObject(new Projectile(ship, ship.getPosition(), velocity, ship.getColor()));
		}
	}
}
