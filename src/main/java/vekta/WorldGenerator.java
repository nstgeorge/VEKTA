package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.object.*;
import vekta.terrain.*;

import static vekta.Vekta.*;

public class WorldGenerator {
	private final float radius; // Max persistent object distance

	public WorldGenerator(int size) {
		this.radius = size;
	}

	public float getRadius() {
		return radius;
	}

	public void spawnOccasional(PVector around) {
		Vekta v = getInstance();
		PVector pos = randomPos(getRadius() / 2, getRadius()).add(around);
		float r = v.random(1);
		if(r > .3F) {
			createSystem(pos);
		}
		else if(r > .1F) {
			Ship s = new CargoShip("TRAWLX", PVector.random2D(), pos, new PVector(), v.color(0, 100, 255));
			addItems(s.getInventory(), 3);
			addObject(s);
		}
		else {
			Ship s = new PirateShip("YARRYACHT", PVector.random2D(), pos, new PVector(), v.color(220, 100, 0));
			addItems(s.getInventory(), 1);
			addObject(s);
		}
	}

	private void createSystem(PVector pos) {
		Vekta v = Vekta.getInstance();
		float order = v.random(29, 32);

		// Create the center body
		float centerPower = (float)Math.pow(10, order);
		float centerMass = v.random(0.8F, 4) * centerPower;
		float centerDensity = v.random(.7F, 2);
		if(order >= 30) {
			addObject(new Star(
					centerMass, // Mass
					centerDensity, // Radius
					pos,
					new PVector(),
					v.color(v.random(100, 255), v.random(150, 255), v.random(100, 255))
			));
		}
		else {
			addObject(new GasGiant(
					centerMass, // Mass
					centerDensity,   // Radius
					pos,  // Position
					new PVector(),  // Velocity
					v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255))
			));
		}

		// Generate planets around body
		int planets = (int)v.random(1, 8);
		for(int i = 0; i <= planets; i++) {
			float power = (float)Math.pow(10, order - 1);
			float radiusLoc = v.random(100, 2000);
			float speed = sqrt(G * centerMass / radiusLoc) / SCALE;
			float mass = v.random(0.05F, 0.5F) * power;
			float density = v.random(4, 8);
			float angle = v.random(360);
			Planet planet = spawnPlanet(mass, density, new PVector(radiusLoc, 0).rotate(angle).add(pos));
			planet.addVelocity(new PVector(0, speed).rotate(angle));
		}
	}

	private Planet spawnPlanet(float mass, float density, PVector pos) {
		Vekta v = Vekta.getInstance();
		float r = v.random(1);
		Terrain terrain;
		if(r > .6) {
			InhabitedTerrain t = new InhabitedTerrain();
			Inventory inv = t.getInventory();
			inv.add((int)v.random(10, 500));
			addItems(inv, 2);
			terrain = t;
		}
		else if(r > .3) {
			AbandonedTerrain t = new AbandonedTerrain();
			addItems(t.getInventory(), 1);
			terrain = t;
		}
		else if(r > .2) {
			terrain = new OceanicTerrain();
		}
		else if(r > .1) {
			terrain = new MiningTerrain();
		}
		else {
			terrain = new MoltenTerrain();
		}
		TerrestrialPlanet planet = new TerrestrialPlanet(
				mass, // Mass
				density,   // Density
				terrain, // Terrain
				pos,  // Coords
				new PVector(),  // Velocity
				v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255))
		);
		addObject(planet);
		return planet;
	}

	private void addItems(Inventory inv, int lootTier) {
		int itemCt = round(getInstance().random(lootTier - 1, lootTier * 2));
		for(int i = 0; i < itemCt; i++) {
			ItemType type = randomItemType();
			// TODO: occasionally add ModuleItems
			Item item = new Item(generateItemName(type), type);
			inv.add(item);
		}
	}

	private ItemType randomItemType() {
		float r = getInstance().random(1);
		if(r < .7) {
			return ItemType.COMMON;
		}
		else if(r < .95) {
			return ItemType.RARE;
		}
		else {
			return ItemType.LEGENDARY;
		}
	}

	private PVector randomPos(float min, float max) {
		return PVector.random2D().mult(getInstance().random(min, max));
	}
}  
