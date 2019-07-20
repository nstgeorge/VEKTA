package vekta.module;

import vekta.InfoGroup;
import vekta.object.PlanetBusterProjectile;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.Planet;
import vekta.object.ship.ModularShip;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class PlanetBusterModule extends WeaponModule {
	private boolean used; // Allow recharging

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
	public Module createVariant() {
		return new PlanetBusterModule();
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void fireWeapon() {
		if(used) {
			return;
		}

		ModularShip ship = getShip();
		Module m = ship.getModule(ModuleType.NAVIGATION);
		if(m instanceof Targeter) {
			SpaceObject target = ((Targeter)m).getTarget();
			if(target instanceof Planet) {
				if(ship.consumeEnergyImmediate(10)) {
					used = true;
					getWorld().playSound("laser", ship.getPosition());
					register(new PlanetBusterProjectile(ship, target, ship.getPosition(), ship.getVelocity(), ship.getColor()));
				}
				else if(ship.hasController()) {
					ship.getController().send("Planet Buster requires more energy");
				}
			}
			else if(ship.hasController()) {
				ship.getController().send("Planet Buster requires a planetary target");
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("The latest innovations in planet-destroying technology have become so devastating that even carrying this around will cause alarm and suspicion.");

		super.onInfo(info);
	}
}
