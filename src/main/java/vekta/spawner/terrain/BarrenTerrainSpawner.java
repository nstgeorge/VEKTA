package vekta.spawner.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.BarrenTerrain;
import vekta.terrain.Terrain;

public class BarrenTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public float getWeight() {
		return 0.2F;
	}

	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return true;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {

		Terrain terrain = new BarrenTerrain(planet);

		return terrain;
	}
}
