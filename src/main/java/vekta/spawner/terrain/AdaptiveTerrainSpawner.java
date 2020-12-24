package vekta.spawner.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.LocationGenerator;
import vekta.spawner.SettlementGenerator;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.AdaptiveTerrain;
import vekta.terrain.Terrain;

public class AdaptiveTerrainSpawner implements TerrainGenerator.TerrainSpawner {
	@Override
	public float getWeight() {
		return 10;
	}

	@Override
	public boolean isValid(TerrestrialPlanet planet) {
		return true;
	}

	@Override
	public Terrain spawn(TerrestrialPlanet planet) {

		AdaptiveTerrain terrain = new AdaptiveTerrain(planet);

		if(terrain.isHabitable()) {
			SettlementGenerator.createSettlement(terrain);
		}

		LocationGenerator.populateLocations(terrain);

		return terrain;
	}
}
