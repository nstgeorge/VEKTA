package vekta;

import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.object.planet.*;
import vekta.object.ship.CargoShip;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;
import vekta.terrain.*;
import vekta.terrain.settlement.RuralSettlement;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.UrbanSettlement;

import static vekta.Vekta.*;

public class WorldGenerator {
	public static float getRadius(RenderLevel dist) {
		return 10000 * getDistanceUnit(dist);
	}

	public static void spawnOccasional(RenderLevel dist, SpaceObject center) {
		PVector pos = randomSpawnPosition(dist, center.getPosition());
		SpaceObject orbit = getWorld().findOrbitObject(center);

		switch(dist) {
		case AROUND_PARTICLE:
			break;
		case AROUND_SHIP:
			if(orbit instanceof TerrestrialPlanet) {
				float r = v.random(1);
				if(r > .4F) {
					int color = v.random(1) < .6F ? orbit.getColor() : randomPlanetColor();
					Ship s = new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), color);
					ItemGenerator.addLoot(s.getInventory(), 3);
					addObject(s);
					orbit(orbit, s, .75F);
				}
				else {
					Ship s = new PirateShip("YARRYACHT", PVector.random2D(), pos, new PVector(), v.color(220, 100, 0));
					ItemGenerator.addLoot(s.getInventory(), 1);
					addObject(s);
					orbit(orbit, s, .75F);
				}
			}
			break;
		case AROUND_PLANET:
			if(orbit != null) {
				Asteroid s = createAsteroid(pos);
				orbit(orbit, s, .5F);
			}
			break;
		case AROUND_STAR:
			createSystem(pos);
			break;
		}
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
		float power = pow(10, v.random(29, 32));
		float mass = v.random(0.8F, 4) * power;
		float density = v.random(.7F, 2);
		Star star = new Star(
				WorldGenerator.randomPlanetName(),
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
				randomPlanetName(),
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
				WorldGenerator.randomPlanetName(),
				mass, // Mass
				density, // Radius
				pos, // Position
				new PVector(), // Velocity
				randomPlanetColor() // Color
		);
		int moonCt = (int)v.random(5, 10);
		for(int i = 0; i < moonCt; i++) {
			float distance = v.random(1, 3) * LUNAR_DISTANCE;
			Planet moon = createMoon(planet, distance);
			orbit(planet, moon, 0);
		}
		addObject(planet);
		return planet;
	}

	public static Moon createMoon(Planet planet, float distance) {
		PVector pos = planet.getPosition().add(PVector.random2D().mult(planet.getRadius() + distance));
		float mass = pow(10, v.random(21, 23));
		float density = v.random(3, 4);
		Moon moon = new Moon(
				planet,
				randomPlanetName(),
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

	public static Asteroid createAsteroid(PVector pos) {
		float mass = pow(10, v.random(15, 20));
		float density = v.random(1.5F, 2);
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
				String feature = v.random(Resources.getStrings("planet_features"));
				for(String s : feature.split(",")) {
					terrain.addFeature(s.trim());
				}
			}
		}
		return terrain;
	}

	public static Settlement createSettlement(Planet planet) {
		float r = v.random(1);
		if(r > .4) {
			return new UrbanSettlement();
		}
		else {
			return new RuralSettlement();
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

	public static PVector randomSpawnPosition(RenderLevel dist, PVector center) {
		float radius = getRadius(dist);
		return PVector.random2D().mult(v.random(radius / 2, radius)).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public static String randomPlanetName() {
		return v.random(Resources.getStrings("planet_prefixes")) + v.random(Resources.getStrings("planet_suffixes", ""));
	}
}  
