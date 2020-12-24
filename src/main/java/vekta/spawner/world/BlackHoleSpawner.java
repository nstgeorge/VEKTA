package vekta.spawner.world;

import processing.core.PVector;
import vekta.spawner.TerrainGenerator;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.BlackHole;

import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.WorldSpawner;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class BlackHoleSpawner implements WorldSpawner {
	@Override
	public float getWeight() {
		return .1F;
	}

	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		createBlackHole(pos);
	}

	public static BlackHole createBlackHole(PVector pos) {
		float mass = v.random(1, 15) * SUN_MASS;
		return register(new BlackHole(
				Resources.generateString("black_hole"),
				mass,
				pos,
				new PVector(),
				v.lerpColor(0, randomPlanetColor(), .7F)
		));
	}
}
