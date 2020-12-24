package vekta.spawner;

import vekta.Resources;
import vekta.terrain.Terrain;

import java.util.Arrays;

import static vekta.Vekta.v;

public class LocationGenerator {
	private static final LocationSpawner[] SPAWNERS;

	static {
		// Load spawners from classpath
		SPAWNERS = Arrays.stream(Resources.findSubclassInstances(LocationSpawner.class))
				.toArray(LocationSpawner[]::new);
	}

	public static void ensureLoaded() {
		// Necessary for eager-loading all FeatureLocationSpawner instances
		// TODO: find a cleaner way to do this
	}

	public static void populateLocations(Terrain terrain) {
		for(LocationSpawner spawner : SPAWNERS) {
			if(spawner.isValid(terrain) && v.chance(spawner.getChance(terrain))) {
				spawner.spawn(terrain);
			}
		}
	}

	public interface LocationSpawner extends Weighted {
		boolean isValid(Terrain terrain);

		default float getChance(Terrain terrain) {
			return 1;
		}

		void spawn(Terrain terrain);
	}
}
