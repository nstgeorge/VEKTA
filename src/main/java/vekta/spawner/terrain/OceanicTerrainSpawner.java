package vekta.spawner.terrain;

import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.OceanicTerrain;
import vekta.terrain.Terrain;
import vekta.terrain.location.OceanLocation;

public class OceanicTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return true;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {

		OceanicTerrain terrain = new OceanicTerrain(planet);

		OceanLocation location = new OceanLocation(planet, planet.getName() + " " + Resources.generateString("ocean_suffix"));

		terrain.addPathway(location, "Ocean");

		return terrain;
	}
}
