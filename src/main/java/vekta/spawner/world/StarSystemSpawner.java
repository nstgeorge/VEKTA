package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.spawner.WorldGenerator;
import vekta.object.SpaceObject;
import vekta.object.planet.*;

import static processing.core.PApplet.pow;
import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.*;

public class StarSystemSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.STAR;
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
		Star star = new Star(
				Resources.generateString("star"),
				mass, // Mass
				density, // Radius
				pos, // Position
				new PVector(), // Velocity
				randomPlanetColor() // TODO: color based on star properties
		);
		addObject(star);
		return star;
	}

	public static Planet createPlanet(PVector pos) {
		float mass = pow(10, v.random(23, 25));
		float density = v.random(3.5F, 4);
		TerrestrialPlanet planet = new TerrestrialPlanet(
				Resources.generateString("planet"),
				mass, // Mass
				density,   // Density
				createTerrain(), // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				randomPlanetColor() // Color
		);
		int moonCt = (int)v.random(3);
		for(int i = 0; i < moonCt; i++) {
			float distance = v.random(.3F, 2) * LUNAR_DISTANCE;
			Planet moon = createMoon(planet, distance);
			orbit(planet, moon, 0);
		}
		addObject(planet);
		return planet;
	}

	public static GasGiant createGasGiant(PVector pos) {
		float mass = pow(10, v.random(26, 28));
		float density = v.random(1.3F, 2);
		GasGiant planet = new GasGiant(
				Resources.generateString("gas_giant"),
				mass, // Mass
				density, // Radius
				pos, // Position
				new PVector(), // Velocity
				randomPlanetColor() // Color
		);
		int moonCt = (int)v.random(3, 6);
		for(int i = 0; i < moonCt; i++) {
			float distance = v.random(1, 20) * LUNAR_DISTANCE;
			Planet moon = createMoon(planet, distance);
			orbit(planet, moon, 0);
		}
		addObject(planet);
		return planet;
	}

	public static Moon createMoon(Planet planet, float distance) {
		PVector pos = planet.getPosition().add(PVector.random2D().mult(planet.getRadius() + distance));
		float mass = pow(10, v.random(18, 23));
		float density = v.random(3, 4);
		Moon moon = new Moon(
				planet,
				Resources.generateString("moon"),
				mass, // Mass
				density,   // Density
				createTerrain(), // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				randomPlanetColor() // Color
		);
		addObject(moon);
		return moon;
	}
}