package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.object.planet.*;
import vekta.object.ship.CargoShip;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;
import vekta.terrain.*;

import static vekta.Vekta.*;

public class WorldGenerator {
	public static float getRadius() {
		return 20000;
	}

	public static void spawnOccasional(PVector around) {
		PVector pos = randomSpawnPosition(around);
		float r = v.random(1);
		if(r > .3F) {
			createSystem(pos);
		}
		else if(r > .1F) {
			Ship s = new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), randomPlanetColor());
			ItemGenerator.addLoot(s.getInventory(), 3);
			addObject(s);
		}
		else {
			Ship s = new PirateShip("YARRYACHT", PVector.random2D(), pos, new PVector(), v.color(220, 100, 0));
			ItemGenerator.addLoot(s.getInventory(), 1);
			addObject(s);
		}
	}

	public static void createSystem(PVector pos) {
		float order = v.random(29, 32);

		// Create the center body
		float centerPower = pow(10, order);
		float centerMass = v.random(0.8F, 4) * centerPower;
		float centerDensity = v.random(.7F, 2);
		if(order >= 30) {
			addObject(new Star(
					WorldGenerator.randomPlanetName(),
					centerMass, // Mass
					centerDensity, // Radius
					pos, // Position
					new PVector(), // Velocity
					randomPlanetColor() // Color
			));
		}
		else {
			addObject(new GasGiant(
					WorldGenerator.randomPlanetName(),
					centerMass, // Mass
					centerDensity, // Radius
					pos, // Position
					new PVector(), // Velocity
					randomPlanetColor() // Color
			));
		}

		// Generate planets around body
		int planets = (int)v.random(1, 8);
		for(int i = 0; i <= planets; i++) {
			float power = pow(10, order - 1);
			float radiusLoc = v.random(1000, 8000);
			float speed = sqrt(G * centerMass / radiusLoc) / SCALE;
			float mass = v.random(0.05F, 0.5F) * power;
			float density = v.random(4, 8);
			float angle = v.random(360);
			Planet planet = createPlanet(mass, density, new PVector(radiusLoc, 0).rotate(angle).add(pos));
			planet.addVelocity(new PVector(0, speed).rotate(angle));
		}

		int asteroids = (int)v.random(20);
		for(int i = 0; i <= asteroids; i++) {
			float power = pow(10, order - v.random(3, 4));
			float radiusLoc = v.random(1000, 15000);
			float speed = sqrt(G * centerMass / radiusLoc) / SCALE * v.random(.75F, 1.5F);
			float mass = v.random(0.05F, 0.5F) * power;
			float density = v.random(2, 4);
			float angle = v.random(360);
			Asteroid asteroid = createAsteroid(mass, density, new PVector(radiusLoc, 0).rotate(angle).add(pos));
			asteroid.addVelocity(new PVector(0, speed).rotate(angle));
		}
	}

	public static Planet createPlanet(float mass, float density, PVector pos) {
		float r = v.random(1);
		Terrain terrain;
		boolean features = true;
		if(r > .5) {
			InhabitedTerrain t = new InhabitedTerrain();
			Inventory inv = t.getInventory();
			inv.add((int)v.random(10, 500));
			ItemGenerator.addLoot(inv, 2);
			terrain = t;
		}
		else if(r > .3) {
			AbandonedTerrain t = new AbandonedTerrain();
			ItemGenerator.addLoot(t.getInventory(), 1);
			terrain = t;
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
				String feature = v.random(Resources.getStrings("planet_features"));
				for(String s : feature.split(",")) {
					terrain.addFeature(s.trim());
				}
			}
		}
		TerrestrialPlanet planet = new TerrestrialPlanet(
				randomPlanetName(),
				mass, // Mass
				density,   // Density
				terrain, // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				randomPlanetColor() // Color
		);
		addObject(planet);
		return planet;
	}

	public static Asteroid createAsteroid(float mass, float density, PVector pos) {
		AsteroidTerrain terrain = new AsteroidTerrain();
		Asteroid asteroid = new Asteroid(
				randomPlanetName(),
				mass,
				density,
				terrain,
				pos,
				new PVector(),
				randomPlanetColor());
		addObject(asteroid);
		return asteroid;
	}

	public static PVector randomSpawnPosition(PVector center) {
		return PVector.random2D().mult(v.random(getRadius() / 2, getRadius())).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public static String randomPlanetName() {
		return v.random(Resources.getStrings("planet_prefixes")) + v.random(Resources.getStrings("planet_suffixes", ""));
	}
}  
