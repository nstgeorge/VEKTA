package vekta.spawner.item;

import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.module.WormholeModule;
import vekta.object.planet.Planet;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.getWorld;

public class WormholeItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof ModuleItem && ((ModuleItem)item).getModule() instanceof WormholeModule;
	}

	@Override
	public Item create() {
		return randomWormholeItem();
	}

	public static Item randomWormholeItem() {
		return new ModuleItem(new WormholeModule(getWorld().findRandomObject(Planet.class)));
	}
}
