package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.object.GasGiant;
import vekta.object.Planet;
import vekta.object.TerrestrialPlanet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.*;

public class UniverseGen {
	private static final int MIN_SPAWN_DISTANCE = 500;

	int size;
	int density;

	public UniverseGen(int size, int density) {
		this.size = size;
		this.density = density;
	}

	public List<Planet> generate() {
		List<Planet> objects = new ArrayList<Planet>();
		for(int i = 0; i < density; i++) {
			objects.addAll(createSystem(generateCoordinates(size)));
		}
		return objects;
	}

	private List<Planet> createSystem(PVector pos) {
		Vekta v = Vekta.getInstance();
		List<Planet> system = new ArrayList<>();
		float order = v.random(29, 32);
		
		// Create the center body
		float centerPower = (float)Math.pow(10, order);
		float centerMass = v.random(0.8F, 4) * centerPower;
		float centerDensity = v.random(1, 2);
		system.add(setupPlanet(new GasGiant(
				centerMass, // Mass
				centerDensity,   // Radius
				pos,  // Position
				new PVector(),  // Velocity
				v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255))
		)));

		// Generate planets around body
		int planets = (int)v.random(1, 8);
		for(int i = 0; i <= planets; i++) {
			float power = (float)Math.pow(10, order - 1);
			float radiusLoc = v.random(100, 2000);
			float speed = sqrt(G * centerMass / radiusLoc) / SCALE;
			float mass = v.random(0.05F, 0.5F) * power;
			float density = v.random(4, 8);
			float angle = v.random(360);
			system.add(setupPlanet(new TerrestrialPlanet(
					mass, // Mass
					density,   // Density
					new PVector(radiusLoc, 0).rotate(angle).add(pos),  // Coords
					new PVector(0, speed).rotate(angle),  // Velocity
					v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255))
			)));
		}
		return system;
	}

	private Planet setupPlanet(GasGiant planet) {
		return planet;
	}
	
	private Planet setupPlanet(TerrestrialPlanet planet) {
		Vekta v = getInstance();
		LandingSite site = planet.getLandingSite();
		Inventory inv = site.getInventory();
		Map<Item, Integer> offers = site.getOffers();

		inv.add((int)v.random(10, 500));
		int itemCt = round(v.random(1, 4));
		for(int i = 0; i < itemCt; i++) {
			ItemType type = randomItemType();
			Item item = new Item(generateItemName(type), type);
			inv.add(item);
			int price = type.randomPrice();
			offers.put(item, price);
		}

		return planet;
	}

	private ItemType randomItemType() {
		float n = getInstance().random(1);
		if(n > .3) {
			return ItemType.COMMON;
		}
		else if(n > .05) {
			return ItemType.RARE;
		}
		else {
			return ItemType.LEGENDARY;
		}
	}

	private PVector generateCoordinates(float max) {
		return PVector.random2D().mult(MIN_SPAWN_DISTANCE + getInstance().random(max - MIN_SPAWN_DISTANCE));
	}
}  
