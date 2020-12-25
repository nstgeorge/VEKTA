package vekta.spawner.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.LocationGenerator;
import vekta.spawner.SettlementGenerator;
import vekta.spawner.TerrainGenerator;
import vekta.terrain.AdaptiveTerrain;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.Settlement;

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

		LocationGenerator.populateLocations(terrain);

		if(terrain.isHabitable()) {
			Settlement settlement = SettlementGenerator.createSettlement(planet);

			terrain.addPathway(settlement.getLocation());//////
		}

		return terrain;
	}
}
