package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.spawner.WorldGenerator;
import vekta.object.SpaceObject;
import vekta.object.planet.Asteroid;
import vekta.terrain.AsteroidTerrain;

import static processing.core.PApplet.pow;
import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class AsteroidSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit != null) {
			Asteroid s = createAsteroid(pos);
			orbit(orbit, s, .5F);
		}
	}

	public static Asteroid createAsteroid(PVector pos) {
		float mass = pow(10, v.random(15, 20));
		float density = v.random(1.5F, 2);
		AsteroidTerrain terrain = new AsteroidTerrain();
		Asteroid asteroid = new Asteroid(
				Resources.generateString("asteroid"),
				mass,
				density,
				terrain,
				pos,
				new PVector(),
				randomPlanetColor());
		addObject(asteroid);
		return asteroid;
	}
}
