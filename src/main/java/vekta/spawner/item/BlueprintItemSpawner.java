package vekta.spawner.item;

import vekta.Resources;
import vekta.item.*;
import vekta.spawner.ItemGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class BlueprintItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Map<String, ItemCategory[]> MODULE_MAP = new HashMap<>();

	static {
		Map<String, List<String>> map = Resources.getStringMap("crafting_module_map", false);
		for(String name : map.keySet()) {
			if(ModuleItemSpawner.findModule(name) == null) {
				throw new RuntimeException("Module not found: `" + name + "`");
			}
			MODULE_MAP.put(name, map.get(name).stream().map(PatternItemCategory::new).toArray(ItemCategory[]::new));
		}
	}

	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.RECIPE;
	}

	@Override
	public Item create() {
		return randomBlueprint();
	}

	public static BlueprintItem randomBlueprint() {
		return randomModuleBlueprint();
	}

	public static BlueprintItem randomModuleBlueprint() {
		String name = v.random(MODULE_MAP.keySet());
		return new BlueprintItem(name, ItemType.MODULE, (p, i) -> new ModuleItem(ModuleItemSpawner.findModule(name).createVariant()), MODULE_MAP.get(name));
	}
}
