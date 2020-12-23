package vekta.spawner.terrain;

import vekta.object.planet.Asteroid;
import vekta.object.planet.GasGiant;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.spawner.location.feature.CloudsFeature;
import vekta.terrain.AsteroidTerrain;
import vekta.terrain.GasGiantTerrain;
import vekta.terrain.Terrain;

public class GasGiantTerrainSpawner implements TerrainGenerator.TerrainSpawner {

	@Override
	public boolean isExclusive() {
		return true;
	}

	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return planet instanceof GasGiant;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {
		return new GasGiantTerrain(planet);
	}
}
