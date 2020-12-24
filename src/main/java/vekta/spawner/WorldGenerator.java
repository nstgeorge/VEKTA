package vekta.spawner;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;

import java.util.Arrays;

import static processing.core.PApplet.max;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.QUARTER_PI;
import static vekta.Vekta.*;

public class WorldGenerator {
	private static final WorldSpawner[][] SPAWNERS;

	static {
		// Load SPAWNERS from classpath
		WorldSpawner[] options = Resources.findSubclassInstances(WorldSpawner.class);
		// Create spawner array for each RenderLevel, indexed by ordinal()
		SPAWNERS = Arrays.stream(RenderLevel.values())
				.map(level -> Arrays.stream(options).filter(s -> s.getObjectLevel() == level).toArray(WorldSpawner[]::new))
				.toArray(WorldSpawner[][]::new);
	}

	public static float getRadius(RenderLevel level) {
		return 10000 * getDistanceUnit(level);
	}

	public static void spawnOccasional(RenderLevel level, SpaceObject center) {
		WorldSpawner[] array = SPAWNERS[level.ordinal()];
		if(array.length > 0) {
			WorldSpawner spawner = Weighted.random(array);
			spawner.spawn(center, randomSpawnPosition(spawner.getSpawnLevel(), center.getPosition()));
		}
	}

	public static void orbit(SpaceObject parent, SpaceObject child, float variation) {
		PVector velocity = getOrbitVelocity(parent, child);
		if(variation > 0) {
			velocity.sub(parent.getVelocity())
					.rotate(v.random(-QUARTER_PI, QUARTER_PI) * variation)
					.add(parent.getVelocity());
		}
		child.setVelocity(velocity);
	}

	public static PVector getOrbitVelocity(SpaceObject parent, SpaceObject child) {
		PVector offset = parent.getPosition().sub(child.getPosition());
		float speed = sqrt(G * parent.getMass() / offset.mag());
		return offset.rotate(HALF_PI).setMag(speed).add(parent.getVelocity());
	}

	public static PVector randomSpawnPosition(RenderLevel level, PVector center) {
		float radius = getRadius(level);
		return PVector.random2D().mult(v.random(radius / 4, radius)).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public interface WorldSpawner extends Weighted {
		RenderLevel getSpawnLevel();

		default RenderLevel getObjectLevel() {
			return getSpawnLevel();
		}

		void spawn(SpaceObject center, PVector pos);
	}
}
