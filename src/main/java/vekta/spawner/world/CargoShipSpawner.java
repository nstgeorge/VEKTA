package vekta.spawner.world;

import processing.core.PVector;
import vekta.faction.Faction;
import vekta.object.SpaceObject;
import vekta.object.ship.CargoShip;
import vekta.object.ship.EscortShip;
import vekta.object.ship.Ship;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class CargoShipSpawner extends NearPlanetSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, LandingSite site) {
		if(site.getTerrain().isInhabited()) {
			Faction faction = v.chance(.25F)
					? v.random(site.getTerrain().findVisitableSettlements()).getFaction()
					: FactionGenerator.randomFaction();

			Ship s = register(new CargoShip(faction.getName() + " Cargo Ship", PVector.random2D(), pos, new PVector(), faction));
			WorldGenerator.orbit(site.getParent(), s, .5F);

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
