package vekta.spawner.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.*;

public class MoltenTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return true;
	}

	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {

		return new MoltenTerrain(planet);
	}
}
