package vekta.spawner.world;

import processing.core.PVector;
import vekta.object.planet.DebrisPlanet;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.RingDebris;
import vekta.object.SpaceObject;
import vekta.object.planet.GasGiant;
import vekta.spawner.WorldGenerator;

import static processing.core.PApplet.pow;
import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class RingDebrisSpawner implements WorldGenerator.WorldSpawner {
	private static  final float ANYWHERE_CHANCE = 0.01F; // Chance to spawn outside of a ring system
	
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(v.chance(ANYWHERE_CHANCE) || (orbit instanceof GasGiant && ((GasGiant)orbit).isInsideRings(pos)) || orbit instanceof DebrisPlanet) {
			SpaceObject s = createDebris(pos, 2, 10);
			orbit(orbit, s, .1F);
		}
	}

	public static RingDebris createDebris(PVector pos, float minPow, float maxPow) {
		float mass = pow(10, v.random(minPow, maxPow));
		float density = v.random(1.2F, 2);
		return register(new RingDebris(
				Resources.generateString("ring_debris"),
				mass,
				density,
				pos,
				new PVector(),
				randomPlanetColor()));
	}
}
