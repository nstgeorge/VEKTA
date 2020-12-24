package vekta.spawner.world;

import processing.core.PVector;
import vekta.faction.Faction;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.CargoShip;
import vekta.object.ship.EscortShip;
import vekta.object.ship.Ship;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class CargoShipSpawner extends NearTerrestrialPlanetSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, TerrestrialPlanet planet) {
		if(planet.isInhabited()) {
			Faction faction = v.chance(.25F)
					? v.random(planet.findInhabitedSettlements()).getFaction()
					: FactionGenerator.randomFaction();

			Ship s = register(new CargoShip(faction.getName() + " Cargo Ship", PVector.random2D(), pos, new PVector(), faction));
			WorldGenerator.orbit(planet, s, .5F);

			int tier = (int)v.random(3) + 1;
			ItemGenerator.addLoot(s.getInventory(), tier);

			int escortCt = (int)v.random(tier, tier * 2) + 1;
			for(int i = 0; i < escortCt; i++) {
				EscortShip escort = register(new EscortShip(
						s.getName() + " Escort",
						s.getHeading(),
						PVector.random2D().mult(s.getRadius() * 10).add(s.getPosition()),
						s.getVelocity(),
						s.getColor()
				));
				escort.setTarget(s); // Follow cargo ship
			}
		}
	}
}
