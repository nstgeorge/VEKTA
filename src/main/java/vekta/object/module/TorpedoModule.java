package vekta.object.module;

import processing.core.PVector;
import vekta.object.HomingProjectile;
import vekta.object.ModularShip;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

import static vekta.Vekta.addObject;
import static vekta.Vekta.getWorld;

public class TorpedoModule extends WeaponModule {
	private final float speed;
	
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
	public Module getVariant() {
		return new TorpedoModule(chooseInclusive(1, 2));
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		Module m = ship.getBestModule(ModuleType.TARGET_COMPUTER);
		if(m instanceof Targeter) {
			SpaceObject target = ((Targeter)m).getTarget();
			if(target != null && ship.consumeEnergy(1)) {
				getWorld().playSound("laser", ship.getPosition());
				PVector velocity = ship.getVelocity().add(PVector.random2D());
				addObject(new HomingProjectile(ship, target, getSpeed(), ship.getPosition(), velocity, ship.getColor()));
			}
		}
	}
}
