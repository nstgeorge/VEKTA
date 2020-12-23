package vekta.spawner.world;

import processing.core.PVector;
import vekta.spawner.TerrainGenerator;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.Asteroid;
import vekta.spawner.WorldGenerator;

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
	public RenderLevel getSpawnLevel() {
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

	//	public static Asteroid createAsteroid(PVector pos) {
	//		Faction faction = FactionGenerator.randomFaction();
	//		Terrain terrain;
	//		float r = v.random(1);
	//		if(r > .8F) {
	//			terrain = new AsteroidTerrain();
	//		}
	//		else if(r > .4F) {
	//			terrain = new AdaptiveTerrain(new ColonySettlement(faction));
	//		}
	//		else if(r > .2F) {
	//			terrain = new AdaptiveTerrain(new OutpostSettlement(faction));
	//		}
	//		else {
	//			terrain = new AdaptiveTerrain(, new EmptySettlement(faction));
	//		}
	//		return createAsteroid(pos, terrain);
	//	}

	public static Asteroid createAsteroid(PVector pos) {
		float mass = pow(10, v.random(15, 20));
		float density = v.random(1500, 2500);
		return register(new Asteroid(
				Resources.generateString("asteroid"),
				mass,
				density,
				pos,
				new PVector(),
				randomPlanetColor()));
	}
}
