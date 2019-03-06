package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.object.SpaceObject;
import vekta.object.planet.Asteroid;
import vekta.object.planet.Planet;
import vekta.object.planet.Star;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.CargoShip;
import vekta.object.ship.PirateShip;
import vekta.object.ship.Ship;
import vekta.terrain.*;

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
					Ship s = new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), randomPlanetColor());
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
		//		else {
		//			// TODO: spawn as part of a system rather than as a star alternative
		//			star = new GasGiant(
		//					WorldGenerator.randomPlanetName(),
		//					centerMass, // Mass
		//					centerDensity, // Radius
		//					pos, // Position
		//					new PVector(), // Velocity
		//					randomPlanetColor() // Color
		//			);
		//		}

		// Generate planets around body
		int planets = (int)v.random(1, 8);
		for(int i = 0; i <= planets; i++) {
			float distance = v.random(.5F, 4) * AU_DISTANCE;
			float angle = v.random(360);
			Planet planet = createPlanet(new PVector(distance, 0).rotate(angle).add(pos));
			orbit(star, planet, 0);
		}
	}

	public static Star createStar(PVector pos) {
		float power = pow(10, v.random(29, 31));
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
		float density = v.random(2, 4);
		TerrestrialPlanet planet = new TerrestrialPlanet(
				randomPlanetName(),
				mass, // Mass
				density,   // Density
				createTerrain(), // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				randomPlanetColor() // Color
		);
		addObject(planet);
		return planet;
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
		float r = v.random(1);
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
		return terrain;
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
