package vekta.spawner.world;

import processing.core.PVector;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.register;

public class PirateShipSpawner extends NearTerrestrialPlanetSpawner {
	private static final int PIRATE_COLOR = DANGER_COLOR;

	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, TerrestrialPlanet planet) {
		if(planet.isInhabited()) {
			Ship s = register(new PirateShip(Resources.generateString("pirate"), PVector.random2D(), pos, new PVector(), PIRATE_COLOR));
			WorldGenerator.orbit(planet, s, .5F);

			ItemGenerator.addLoot(s.getInventory(), 1);
		}
	}
}
