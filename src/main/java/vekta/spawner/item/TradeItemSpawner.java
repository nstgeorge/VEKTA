package vekta.spawner.item;

import vekta.Resources;
import vekta.spawner.ItemGenerator;
import vekta.item.Item;
import vekta.item.ItemType;

import static vekta.Vekta.v;

public class TradeItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 3;
	}

	@Override
	public Item create() {
		ItemType type = randomType();
		return new Item(randomName(type), type);
	}

	public static ItemType randomType() {
		float r = v.random(1);
		if(r > .3) {
			return ItemType.COMMON;
		}
		else if(r > .05) {
			return ItemType.RARE;
		}
		else {
			return ItemType.LEGENDARY;
		}
	}

	public static String randomName(ItemType type) {
		return Resources.generateString("item_" + type.name().toLowerCase());
	}
}
