package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.module.Module;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class ModuleItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Module[] MODULES = Resources.getSubclassInstances(Module.class);

	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.MODULE;
	}

	@Override
	public Item create() {
		return new ModuleItem(randomModule());
	}

	public static Module randomModule() {
		return v.random(MODULES).getVariant();
	}
}
