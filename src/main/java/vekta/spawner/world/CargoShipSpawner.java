package vekta.spawner.world;

import processing.core.PVector;
import vekta.Faction;
import vekta.object.ship.CargoShip;
import vekta.object.ship.Ship;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public abstract class CargoShipSpawner extends ShipSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(LandingSite site, PVector pos) {
		// Only spawn near settlements
		if(!site.getTerrain().getSettlements().isEmpty()) {
			Faction faction = v.chance(.8F)
					? v.random(site.getTerrain().getSettlements()).getFaction()
					: FactionGenerator.randomFaction();

			Ship s = register(new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), faction));
			WorldGenerator.orbit(site.getParent(), s, .25F);

			ItemGenerator.addLoot(s.getInventory(), 2);
		}
	}
}
