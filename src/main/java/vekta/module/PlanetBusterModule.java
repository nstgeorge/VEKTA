package vekta.module;

import vekta.object.PlanetBusterProjectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.Planet;
import vekta.object.ship.ModularShip;
import vekta.util.InfoGroup;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class PlanetBusterModule extends WeaponModule {
	private boolean used; // TODO: allow recharging

	public PlanetBusterModule() {
	}

	@Override
	public String getName() {
		return "Planet Buster Missile" + (used ? " (discharged)" : "");
	}

	@Override
	public int getMass() {
		return used ? 100 : 2000;
	}

	@Override
	public BaseModule createVariant() {
		return new PlanetBusterModule();
	}

	@Override
	public float getValueScale() {
		return 5;
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return false;
	}

	@Override
	public void fireWeapon() {
		if (used) {
			return;
		}

		ModularShip ship = getShip();
		BaseModule m = ship.getModule(ModuleType.NAVIGATION);
		if (m instanceof Targeter) {
			SpaceObject target = ((Targeter) m).getTarget();
			if (target instanceof Planet) {
				if (ship.consumeEnergyImmediate(20)) {
					used = true;
					getWorld().playSound("laser", ship.getPosition());
					register(new PlanetBusterProjectile(ship, target, ship.getPosition(), ship.getVelocity(), ship.getColor()));
				} else if (ship.hasController()) {
					ship.getController().send(getName() + " requires more energy");
				}
			} else if (ship.hasController()) {
				ship.getController().send(getName() + " requires a planetary target");
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription(
				"The latest innovations in planet-destroying technology have become so devastating that even carrying this around will cause alarm and suspicion.");

		super.onInfo(info);
	}
}
