package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.WeaponItem;
import vekta.spawner.ItemGenerator;

import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class WeaponItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Map<String, List<String>> WEAPONS = Resources.getStringMap("item_weapon", false);

	@Override
	public float getWeight() {
		return .1F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof WeaponItem;
	}

	@Override
	public Item create() {
		return randomWeapon();
	}

	public static WeaponItem randomWeapon() {
		String key = v.random(WEAPONS.keySet());
		String name = key;
		if(v.chance(.5F)) {
			name = Resources.generateString("item_adj_common") + " " + name;
		}
		return new WeaponItem(name, v.random(WEAPONS.get(key)).replace("*", name));
	}
}
