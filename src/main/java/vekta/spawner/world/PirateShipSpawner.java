package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.spawner.WorldGenerator;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;

import static vekta.Vekta.*;
import static vekta.spawner.ItemGenerator.addLoot;
import static vekta.spawner.WorldGenerator.orbit;

public class PirateShipSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit instanceof TerrestrialPlanet && ((TerrestrialPlanet)orbit).isHabitable()) {
			// Only spawn near terrestrial planets
			Ship s = new PirateShip("YARRYACHT", PVector.random2D(), pos, new PVector(), v.color(220, 100, 0));
			addObject(s);
			orbit(orbit, s, .5F);

			addLoot(s.getInventory(), 1);
			
		}
	}
}
