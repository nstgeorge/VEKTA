package vekta.spawner.item;

import vekta.Resources;
import vekta.spawner.ItemGenerator;
import vekta.item.Item;
import vekta.item.ModuleItem;
import vekta.module.Module;

import static vekta.Vekta.v;

public class ModuleItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Module[] MODULES = Resources.getSubclassInstances(Module.class);

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public Item create() {
		return new ModuleItem(randomModule());
	}
	
	public static Module randomModule() {
		return v.random(MODULES).getVariant();
	}
}
