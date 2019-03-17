package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class TradeItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return 5;
	}

	@Override
	public boolean isValid(Item item) {
		switch(item.getType()) {
		case COMMON:
		case RARE:
		case LEGENDARY:
			return true;
		}
		return false;
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
