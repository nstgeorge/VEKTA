package vekta.spawner.world;

import processing.core.PVector;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.object.planet.*;
import vekta.spawner.WorldGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static processing.core.PApplet.pow;
import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.*;

public class StarSystemSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		createSystem(pos);
	}

	public static void createSystem(PVector pos) {
		Star star = createStar(pos);

		// Generate terrestrial planets
		int planetCt = (int)v.random(1, 8);
		for(int i = 0; i <= planetCt; i++) {
			float distance = v.random(.5F, 4) * AU_DISTANCE;
			Planet planet = createPlanet(star.getPosition().add(PVector.random2D().mult(distance)));
			orbit(star, planet, 0);
		}

		// Generate gas giants
		int gasGiantCt = (int)v.random(2);
		for(int i = 0; i <= gasGiantCt; i++) {
			float distance = v.random(3, 6) * AU_DISTANCE;
			Planet planet = createGasGiant(star.getPosition().add(PVector.random2D().mult(distance)));
			orbit(star, planet, 0);
		}
	}

	public static Star createStar(PVector pos) {
		float power = pow(10, v.random(29, 31.5F));
		float mass = v.random(0.8F, 4) * power;
		float density = v.random(.7F, 2);
		return register(new Star(
				Resources.generateString("star"),
				mass, // Mass
				density, // Radius
				pos, // Position
				new PVector(), // Velocity
				randomPlanetColor() // TODO: color based on star properties
		));
	}

	public static Planet createPlanet(PVector pos) {
		float mass = pow(10, v.random(23, 25));
		float density = v.random(3.5F, 4);
		TerrestrialPlanet planet = register(new TerrestrialPlanet(
				Resources.generateString("planet"),
				mass,                    // Mass
				density,                // Density
				createPlanetTerrain(),    // Terrain
				pos,                    // Coords
				new PVector(),        // Velocity
				randomPlanetColor()    // Color
		));
		createMoons(planet, (int)v.random(3), .3F * LUNAR_DISTANCE, 2 * LUNAR_DISTANCE);
		return planet;
	}

	public static GasGiant createGasGiant(PVector pos) {
		float mass = pow(10, v.random(26, 28));
		float density = v.random(1.3F, 2);
		GasGiant planet = register(new GasGiant(
				Resources.generateString("gas_giant"),
				mass, // Mass
				density, // Radius
				pos, // Position
				new PVector(), // Velocity
				randomPlanetColor() // Color
		));
		createMoons(planet, (int)v.random(3, 6), 1 * LUNAR_DISTANCE, 20 * LUNAR_DISTANCE);
		return planet;
	}

	public static List<Moon> createMoons(Planet planet, int count, float minDist, float maxDist) {
		List<Moon> moons = new ArrayList<>();
		for(int i = 0; i < count; i++) {
			float distance = v.random(minDist, maxDist);
			Moon moon = createMoon(planet, distance);
			orbit(planet, moon, 0);
			moons.add(moon);
		}
		moons.sort(Comparator.comparingDouble(Moon::getOrbitDistance));
		for(int i = 0; i < moons.size(); i++) {
			moons.get(i).setName((i + 1) + " " + planet.getName());
		}
		return moons;
	}

	public static Moon createMoon(Planet planet, float distance) {
		PVector pos = planet.getPosition().add(PVector.random2D().mult(planet.getRadius() + distance));
		float mass = pow(10, v.random(18, 23));
		float density = v.random(3, 4);
		return register(new Moon(
				planet,
				Resources.generateString("moon"),
				mass, // Mass
				density,   // Density
				createPlanetTerrain(), // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				randomPlanetColor() // Color
		));
	}
}
