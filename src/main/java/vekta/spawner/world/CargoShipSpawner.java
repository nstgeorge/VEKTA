package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.CargoShip;
import vekta.object.ship.Ship;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.*;
import static vekta.spawner.ItemGenerator.addLoot;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class CargoShipSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit instanceof TerrestrialPlanet && ((TerrestrialPlanet)orbit).isHabitable()) {
			// Only spawn near terrestrial planets
			int color = v.random(1) < .6F ? orbit.getColor() : randomPlanetColor();
			Ship s = register(new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), color));
			orbit(orbit, s, .5F);

			addLoot(s.getInventory(), 3);
		}
	}
}
