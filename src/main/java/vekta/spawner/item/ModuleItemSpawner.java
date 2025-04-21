package vekta.spawner.item;

import static vekta.Vekta.v;

import java.util.Arrays;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.module.BaseModule;
import vekta.spawner.ItemGenerator;

public class ModuleItemSpawner implements ItemGenerator.ItemSpawner {
	private static final BaseModule[] MODULES = Resources.findSubclassInstances(BaseModule.class);

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

	public static BaseModule[] getModulePrototypes() {
		return MODULES;
	}

	public static BaseModule randomModule() {
		return v.random(MODULES).createVariant();
	}

	public static BaseModule findModule(String name) {
		return Arrays.stream(MODULES)
				.filter(m -> m.getName().contains(name)).findFirst().orElse(null);
	}
}
