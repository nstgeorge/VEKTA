package vekta.spawner.terrain;

import vekta.object.planet.Asteroid;
import vekta.object.planet.BlackHole;
import vekta.object.planet.Moon;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.AsteroidTerrain;
import vekta.terrain.BlackHoleTerrain;
import vekta.terrain.Terrain;

public class AsteroidTerrainSpawner implements TerrainGenerator.TerrainSpawner {

	@Override
	public boolean isExclusive() {
		return true;
	}

	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return planet instanceof Asteroid;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {
		return new AsteroidTerrain(planet);
	}
}
