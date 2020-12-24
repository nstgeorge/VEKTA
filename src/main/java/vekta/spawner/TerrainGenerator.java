package vekta.spawner;

import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.Terrain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TerrainGenerator {
	private static final TerrainSpawner[] EXCLUSIVE_SPAWNERS;
	private static final TerrainSpawner[] GENERAL_SPAWNERS;

	static {
		// Load spawners from classpath
		TerrainSpawner[] spawners = Arrays.stream(Resources.findSubclassInstances(TerrainSpawner.class))
				.toArray(TerrainSpawner[]::new);

		EXCLUSIVE_SPAWNERS = Arrays.stream(spawners).filter(TerrainSpawner::isExclusive).toArray(TerrainSpawner[]::new);
		GENERAL_SPAWNERS = Arrays.stream(spawners).filter(terrainSpawner -> !terrainSpawner.isExclusive()).toArray(TerrainSpawner[]::new);
	}

	public static Terrain createTerrain(TerrestrialPlanet planet) {
		// Exclusive (bypasses random selection) valid spawners
		List<TerrainSpawner> spawners = Arrays.stream(EXCLUSIVE_SPAWNERS)
				.filter(spawner -> spawner.isValid(planet))
				.collect(Collectors.toList());

		if(spawners.isEmpty()) {
			// All non-exclusive valid spawners
			spawners = Arrays.stream(GENERAL_SPAWNERS)
					.filter(spawner -> spawner.isValid(planet))
					.collect(Collectors.toList());
		}

		return Weighted.random(spawners).spawn(planet);
	}

	public interface TerrainSpawner extends Weighted {

		default boolean isExclusive() {
			return false;
		}

		boolean isValid(TerrestrialPlanet planet);

		Terrain spawn(TerrestrialPlanet planet);
	}
}
