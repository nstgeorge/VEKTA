package vekta.spawner.terrain;

import vekta.object.planet.BlackHole;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.BlackHoleTerrain;
import vekta.terrain.OceanicTerrain;
import vekta.terrain.Terrain;

public class BlackHoleTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public boolean isExclusive() {
		return true;
	}

	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return planet instanceof BlackHole;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {
		return new BlackHoleTerrain(planet);
	}
}
