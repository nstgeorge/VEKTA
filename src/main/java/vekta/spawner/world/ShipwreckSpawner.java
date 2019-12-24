package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.item.ModuleItem;
import vekta.object.SpaceObject;
import vekta.object.ship.LostShip;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.spawner.item.JunkItemSpawner;

import static vekta.Vekta.*;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.item.ModuleItemSpawner.randomModule;

public class ShipwreckSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return .02F;
	}

	@Override
	public RenderLevel getSpawnLevel() {
		return RenderLevel.PLANET;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit != null) {
			SpaceObject s = createShipwreck(PVector.random2D(), pos);
			orbit(orbit, s, .5F);
		}
	}

	public static LostShip createShipwreck(PVector heading, PVector pos) {
		LostShip s = new LostShip(Resources.generateString("shipwreck"), heading, pos, new PVector());
		int junkCt = round(v.random(1, 5));
		for(int i = 0; i < junkCt; i++) {
			s.getInventory().add(ItemGenerator.getSpawner(JunkItemSpawner.class).create());
		}
		s.getInventory().add(new ModuleItem(randomModule()));
		ItemGenerator.addLoot(s.getInventory(), 2);
		return register(s);
	}
}
