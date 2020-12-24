package vekta.spawner.world;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.getWorld;

public abstract class NearTerrestrialPlanetSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public RenderLevel getObjectLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit instanceof TerrestrialPlanet && ((TerrestrialPlanet)orbit).getTerrain().isInhabited()) {
			spawn(center, pos, ((TerrestrialPlanet)orbit));
		}
	}

	public abstract void spawn(SpaceObject center, PVector pos, TerrestrialPlanet planet);
}
