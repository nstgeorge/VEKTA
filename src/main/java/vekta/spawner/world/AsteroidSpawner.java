package vekta.spawner.world;

import processing.core.PVector;
import vekta.faction.Faction;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.Asteroid;
import vekta.spawner.FactionGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.AsteroidTerrain;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.ColonySettlement;
import vekta.terrain.settlement.EmptySettlement;
import vekta.terrain.settlement.OutpostSettlement;

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

	public static Asteroid createAsteroid(PVector pos) {
		Faction faction = FactionGenerator.randomFaction();
		Terrain terrain;
		float r = v.random(1);
		if(r > .8F) {
			terrain = new AsteroidTerrain();
		}
		else if(r > .4F) {
			terrain = new HabitableTerrain(new ColonySettlement(faction));
		}
		else if(r > .2F) {
			terrain = new HabitableTerrain(new OutpostSettlement(faction));
		}
		else {
			terrain = new HabitableTerrain(new EmptySettlement(faction));
		}
		terrain.addFeature("Asteroid");
		return createAsteroid(pos, terrain);
	}

	public static Asteroid createAsteroid(PVector pos, Terrain terrain) {
		float mass = pow(10, v.random(15, 20));
		float density = v.random(1.5F, 2);
		return register(new Asteroid(
				Resources.generateString("asteroid"),
				mass,
				density,
				terrain,
				pos,
				new PVector(),
				randomPlanetColor()));
	}
}
