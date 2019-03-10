package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.OreItem;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class OreItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof OreItem;
	}

	@Override
	public Item create() {
		return randomOre(Resources.generateString("planet"));
	}

	public static Item randomOre(String planetName) {
		ItemType type = v.chance(.8F) ? ItemType.COMMON : ItemType.RARE;
		String key = "ore_" + (type == ItemType.COMMON ? "common" : "rare");

		String[] split = Resources.generateString(key).split(":");
		String name = split[0].trim();
		String refined = split.length > 1 ? split[1].trim() : null;

		if(v.chance(.1F)) {
			type = ItemType.LEGENDARY;
			name += " from " + planetName;
			if(refined != null) {
				refined = planetName + " " + refined;
			}
		}

		return new OreItem(name, refined != null ? new Item(refined, type.getImproved()) : null, type);
	}
}
