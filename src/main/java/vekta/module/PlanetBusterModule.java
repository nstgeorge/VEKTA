package vekta.module;

import vekta.object.PlanetBusterProjectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class PlanetBusterModule extends WeaponModule {
	public PlanetBusterModule() {
	}

	@Override
	public String getName() {
		return "Planet Buster Missile";
	}

	@Override
	public Module getVariant() {
		return new PlanetBusterModule();
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void fireWeapon() {
		ModularShip ship = getShip();
		Module m = ship.getModule(ModuleType.TARGET_COMPUTER);
		if(m instanceof Targeter) {
			SpaceObject target = ((Targeter)m).getTarget();
			if(target != null && ship.consumeEnergyImmediate(10)) {
				getWorld().playSound("laser", ship.getPosition());
				register(new PlanetBusterProjectile(ship, target, ship.getPosition(), ship.getVelocity(), ship.getColor()));
				
//				ship.removeModule(this);///
			}
		}
	}
}
