package vekta.spawner.world;

import processing.core.PVector;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.*;

public class PirateShipSpawner extends ShipSpawner {
	private static final int PIRATE_COLOR = DANGER_COLOR;
	
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(LandingSite site, PVector pos) {
		if(!site.getTerrain().getSettlements().isEmpty()) {
			Ship s = register(new PirateShip("YARRYACHT", PVector.random2D(), pos, new PVector(), PIRATE_COLOR));
			WorldGenerator.orbit(site.getParent(), s, .25F);

			ItemGenerator.addLoot(s.getInventory(), 1);
		}
	}
}
