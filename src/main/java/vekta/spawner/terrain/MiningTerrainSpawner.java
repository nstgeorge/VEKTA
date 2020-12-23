package vekta.spawner.terrain;

import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.MiningTerrain;
import vekta.terrain.Terrain;
import vekta.terrain.location.MiningLocation;

public class MiningTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return true;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {

		Terrain terrain = new MiningTerrain(planet);

		terrain.addPathway(new MiningLocation(planet, planet.getName() + " " + Resources.generateString("mine_suffix")));

		return terrain;
	}
}
