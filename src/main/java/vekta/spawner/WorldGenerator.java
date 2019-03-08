package vekta.spawner;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.Planet;
import vekta.terrain.*;
import vekta.terrain.settlement.AbandonedSettlement;
import vekta.terrain.settlement.RuralSettlement;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.UrbanSettlement;

import java.util.Arrays;

import static vekta.Vekta.*;

public class WorldGenerator {
	private static final WorldSpawner[][] SPAWNERS; // Externally defined weighted world spawners

	static {
		// Load SPAWNERS from classpath
		WorldSpawner[] options = Resources.getSubclassInstances(WorldSpawner.class);
		// Create spawner array for each RenderLevel, indexed by ordinal()
		SPAWNERS = Arrays.stream(RenderLevel.values())
				.map(level -> Arrays.stream(options).filter(s -> s.getRenderLevel() == level).toArray(WorldSpawner[]::new))
				.toArray(WorldSpawner[][]::new);
	}

	public static float getRadius(RenderLevel dist) {
		return 10000 * getDistanceUnit(dist);
	}

	public static void spawnOccasional(RenderLevel dist, SpaceObject center) {
		PVector pos = randomSpawnPosition(dist, center.getPosition());

		WorldSpawner[] array = SPAWNERS[dist.ordinal()];
		if(array.length > 0) {
			Weighted.random(array).spawn(center, pos);
		}
	}

	public static Terrain createTerrain() {
		Terrain terrain;
		boolean features = true;
		float r = v.random(1);
		if(r > .3) {
			terrain = new HabitableTerrain();
		}
		else if(r > .2) {
			terrain = new MiningTerrain();
		}
		else if(r > .1) {
			terrain = new OceanicTerrain();
			features = false;
		}
		else {
			terrain = new MoltenTerrain();
			features = false;
		}
		if(features) {
			int featureCt = (int)v.random(1, 4);
			for(int i = 0; i < featureCt; i++) {
				String feature = Resources.generateString("planet_feature");
				for(String s : feature.split(",")) {
					terrain.addFeature(s.trim());
				}
			}
		}
		return terrain;
	}

	public static Settlement createSettlement(Planet planet) {
		float r = v.random(1);
		if(r > .5) {
			return new UrbanSettlement();
		}
		else if(r > .2) {
			return new RuralSettlement();
		}
		else {
			return new AbandonedSettlement();
		}
	}

	public static void orbit(SpaceObject parent, SpaceObject child, float variation) {
		PVector offset = parent.getPosition().sub(child.getPosition());
		float speed = sqrt(G * parent.getMass() / offset.mag());
		if(variation > 0) {
			offset.rotate(v.random(-QUARTER_PI, QUARTER_PI) * variation);
		}
		child.setVelocity(offset.rotate(HALF_PI).setMag(speed).add(parent.getVelocity()));
	}

	public static PVector randomSpawnPosition(RenderLevel level, PVector center) {
		float radius = getRadius(level);
		return PVector.random2D().mult(v.random(radius / 2, radius)).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public interface WorldSpawner extends Weighted {
		RenderLevel getRenderLevel();

		void spawn(SpaceObject center, PVector pos);
	}
}
